package io.github.dddplus.model;

import io.github.dddplus.model.DirtyMemento;
import io.github.dddplus.model.IDirtyHint;
import io.github.dddplus.model.IMergeAwareDirtyHint;
import io.github.dddplus.model.IUnboundedDomainModel;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DirtyMementoTest {

    @Test
    public void basic() {
        CheckTaskAggregateRoot task = new CheckTaskAggregateRoot();
        assertTrue(task.getDirtyMemento().isEmpty());
        task.bindTo("123");
        assertFalse(task.getDirtyMemento().isEmpty());
        DirtyMemento memento = task.getDirtyMemento();
        assertEquals(1, memento.size());
        task.confirmChecked(5);
        assertEquals(2, memento.size());
        assertSame(memento, task.getDirtyMemento());

        assertFalse(task.qtySaved);
        assertFalse(task.soNoSaved);
        CheckTaskRepository repository = new CheckTaskRepository();
        repository.save(task);
        assertTrue(task.qtySaved);
        assertTrue(task.soNoSaved);

        assertTrue(memento.isEmpty());
        assertTrue(task.getDirtyMemento().isEmpty());

        // repo.save 可以重复调用，幂等
        task.qtySaved = false;
        repository.save(task);
        assertFalse(task.qtySaved);

        try {
            memento.register(null);
            fail();
        } catch (NullPointerException expected) {
            assertEquals("hint is marked non-null but is null", expected.getMessage());
        }
    }

    @Test
    public void firstHintOf() {
        DirtyMemento memento = new DirtyMemento();
        assertNull(memento.firstHintOf(BindSoHint.class));
        assertNull(memento.firstHintOf(ConfirmCheckHint.class));
        memento.register(new BindSoHint("102"));
        BindSoHint bindSoHint = memento.firstHintOf(BindSoHint.class);
        assertEquals("102", bindSoHint.soNo);
        assertNull(memento.firstHintOf(ConfirmCheckHint.class));
        // memento.fistHintOf(String.class); cannot compile
        memento.clear();
        assertNull(memento.firstHintOf(BindSoHint.class));

        memento.register(new BindSoHint("1"));
        memento.register(new BindSoHint("2"));
        bindSoHint = memento.firstHintOf(BindSoHint.class);
        assertEquals("1", bindSoHint.soNo); // not 2
        memento.register(new ConfirmCheckHint(5));
        ConfirmCheckHint confirmCheckHint = memento.firstHintOf(ConfirmCheckHint.class);
        assertEquals(5, confirmCheckHint.qty.intValue());
        memento.clear();
        assertNull(memento.firstHintOf(ConfirmCheckHint.class));
        assertNull(memento.firstHintOf(BindSoHint.class));
    }

    @Test
    public void merge() {
        DirtyMemento memento = new DirtyMemento();
        // 夹杂一个非 merge aware hint
        memento.register(new BindSoHint("1"));

        CheckTask task = new CheckTask();
        task.taskNo = "CK-125";
        task.status = 5;
        CheckTaskUpdateHint hint = new CheckTaskUpdateHint(task);
        hint.dirty("status");
        memento.merge(hint);
        assertEquals(memento.size(), 2);

        task.batchNo = "B-98";
        hint = new CheckTaskUpdateHint(task);
        hint.dirty("batchNo");
        memento.merge(hint);
        assertEquals(memento.size(), 2);
        assertEquals("B-98", memento.firstHintOf(CheckTaskUpdateHint.class).checkTask.batchNo);
        task.checkedQty = BigDecimal.valueOf(55);
        hint = new CheckTaskUpdateHint(task);
        hint.dirty("checkedQty");
        memento.merge(hint);
        assertEquals(BigDecimal.valueOf(55), memento.firstHintOf(CheckTaskUpdateHint.class).checkTask.checkedQty);
        assertEquals(2, memento.size());

        task.id = 5L;
        MergeHint mergeHint = new MergeHint(task);
        memento.merge(mergeHint);
        assertEquals(memento.size(), 3);
        task.status = 8;
        MergeHint mergeHint1 = new MergeHint(task);
        memento.merge(mergeHint1);
        assertEquals(memento.firstHintOf(MergeHint.class).task.status, new Integer(8));

        // 校验 onMerge方法的执行情况
        hint = memento.firstHintOf(CheckTaskUpdateHint.class);
        assertEquals(hint.dirtyFields.size(), 3);
        assertEquals("status", hint.dirtyFields.get(0));
        assertEquals("batchNo", hint.dirtyFields.get(1));
        assertEquals("checkedQty", hint.dirtyFields.get(2));

        assertEquals(memento.firstHintOf(BindSoHint.class).soNo, "1");
    }

    @Test
    public void mergeArgNonNull() {
        DirtyMemento dirtyMemento = new DirtyMemento();
        try {
            dirtyMemento.merge(null);
            fail();
        } catch (NullPointerException expected) {
            assertEquals("hint is marked non-null but is null", expected.getMessage());
        }
    }

    @Test
    public void dirtyHintsOf() {
        DirtyMemento memento = new DirtyMemento();
        assertNotNull(memento.dirtyHintsOf(BindSoHint.class));
        assertTrue(memento.dirtyHintsOf(ConfirmCheckHint.class).isEmpty());
        memento.register(new BindSoHint("1"));
        memento.register(new BindSoHint("2"));
        //List<BindSoHint> hints = memento.dirtyHintsOf(ConfirmCheckHint.class); // will not compile
        List<BindSoHint> hints = memento.dirtyHintsOf(BindSoHint.class);
        assertEquals(2, hints.size());
        assertEquals("1", hints.get(0).soNo);
        assertEquals("2", hints.get(1).soNo);
        memento.clear();
        assertTrue(memento.dirtyHintsOf(BindSoHint.class).isEmpty());
    }

    private static class CheckTaskPo {
        private String soNo;
        private Integer checkQty;
    }

    private static class CheckTaskRepository {
        public void save(CheckTaskAggregateRoot task) {
            DirtyMemento memento = task.getDirtyMemento();
            if (memento.isEmpty()) {
                // bingo! 没有脏数据，不用落库了
                return;
            }

            Map<Class, HintSaver> dispatchTable = new HashMap<>();
            dispatchTable.put(ConfirmCheckHint.class, new ConfirmCheckHintSaver());
            dispatchTable.put(BindSoHint.class, new BindSoHintSaver());

            CheckTaskPo po = new CheckTaskPo();

            // 通过遍历，we got a chance to merge multiple DAO requests
            for (IDirtyHint hint : memento.dirtyHints()) {
                dispatchTable.get(hint.getClass()).saveOn(hint, task, po);
            }

            // checkTaskDao.save(po);
            memento.clear();
        }
    }

    interface HintSaver {
        void saveOn(IDirtyHint hint, CheckTaskAggregateRoot task, CheckTaskPo po);
    }

    static class ConfirmCheckHintSaver extends CheckTaskRepository implements HintSaver {

        @Override
        public void saveOn(IDirtyHint dirtyHint, CheckTaskAggregateRoot task, CheckTaskPo po) {
            ConfirmCheckHint hint = (ConfirmCheckHint) dirtyHint;
            po.checkQty = hint.qty;
            task.qtySaved = true;
            // update by example
            // update if checkQty != null set checkQty=#checkQty
        }
    }

    static class BindSoHintSaver extends CheckTaskRepository implements HintSaver {

        @Override
        public void saveOn(IDirtyHint dirtyHint, CheckTaskAggregateRoot task, CheckTaskPo po) {
            BindSoHint hint = (BindSoHint) dirtyHint;
            task.soNoSaved = true;
            po.soNo = hint.soNo;
        }
    }

    private static class CheckTaskAggregateRoot {
        private DirtyMemento dirtyMemento = new DirtyMemento();

        private String soNo;
        private Integer checkQty;
        private boolean soNoSaved = false;
        private boolean qtySaved = false;

        public void bindTo(String soNo) {
            this.soNo = soNo;
            dirtyMemento.register(new BindSoHint(soNo));
        }

        public void confirmChecked(Integer confirmedQty) {
            this.checkQty = confirmedQty;
            dirtyMemento.register(new ConfirmCheckHint(confirmedQty));
        }

        public DirtyMemento getDirtyMemento() {
            return dirtyMemento;
        }

    }

    private static class BindSoHint implements IDirtyHint {
        private String soNo;
        public BindSoHint(String soNo) {
            this.soNo = soNo;
        }
    }

    private static class ConfirmCheckHint implements IDirtyHint {
        private Integer qty;

        public ConfirmCheckHint(Integer confirmedQty) {
            this.qty = confirmedQty;
        }
    }

    static class CheckTask implements IUnboundedDomainModel {
        Long id;
        String taskNo;
        String batchNo;
        Integer status;
        BigDecimal checkedQty;
    }

    static class MergeHint implements IMergeAwareDirtyHint<Long> {
        final CheckTask task;

        MergeHint(CheckTask task) {
            this.task = task;
        }

        @Override
        public Long getId() {
            return task.id;
        }

        @Override
        public void onMerge(IDirtyHint thatHint) {
            
        }
    }


    static class CheckTaskUpdateHint implements IMergeAwareDirtyHint<String> {
        final CheckTask checkTask;
        List<String> dirtyFields = new LinkedList<>();

        CheckTaskUpdateHint(CheckTask checkTask) {
            this.checkTask = checkTask;
        }
        
        @Override
        public String getId() {
            return checkTask.taskNo;
        }

        @Override
        public void onMerge(IDirtyHint thatHint) {
            CheckTaskUpdateHint that = (CheckTaskUpdateHint) thatHint;
            that.dirtyFields.addAll(this.dirtyFields);
        }

        public void dirty(String field) {
            dirtyFields.add(field);
        }

    }

}