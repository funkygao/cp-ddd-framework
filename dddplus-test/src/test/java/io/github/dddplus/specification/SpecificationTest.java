package io.github.dddplus.specification;

import io.github.dddplus.runtime.registry.mock.model.FooModel;
import io.github.dddplus.specification.mock.B2CMustHavePartnerCode;
import io.github.dddplus.specification.mock.CannotThrowOOMAndRedecideAtTheSameTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@Slf4j
public class SpecificationTest {

    @Test
    public void basic() {
        FooModel fooModel = new FooModel();
        // 业务收益：CannotThrowOOMAndRedecideAtTheSameTime，这个类有了名字，业务规则显性化！
        // 此外，CannotThrowOOMAndRedecideAtTheSameTime也可以声明为Spring bean
        CannotThrowOOMAndRedecideAtTheSameTime specification = new CannotThrowOOMAndRedecideAtTheSameTime();
        Notification notification = Notification.create();
        assertTrue(specification.satisfiedBy(fooModel, notification));
        assertTrue(notification.isEmpty());

        fooModel.setWillThrowOOM(true);
        fooModel.setRedecide(true);
        assertFalse(specification.satisfiedBy(fooModel, notification));
        assertEquals(CannotThrowOOMAndRedecideAtTheSameTime.REASON, notification.firstReason());

        // 如果不关心错误内容，可以把 Notification 通过null传入
        assertFalse(specification.satisfiedBy(fooModel, null));
    }

    @Test
    public void mashupDemo() {
        List<ISpecification<FooModel>> specifications = new ArrayList<>(2);
        specifications.add(new CannotThrowOOMAndRedecideAtTheSameTime());
        specifications.add(new B2CMustHavePartnerCode());

        FooModel fooModel = new FooModel();
        fooModel.setB2c(true);
        for (ISpecification<FooModel> specification : specifications) {
            if (!specification.satisfiedBy(fooModel)) {
                log.error("specification:{}", specification.getClass().getCanonicalName());
                break;
            }
        }
    }

}