package ddd.plus.showcase.wms.domain.task.spec;

import ddd.plus.showcase.wms.domain.common.Operator;
import ddd.plus.showcase.wms.domain.common.WmsException;
import ddd.plus.showcase.wms.domain.common.gateway.IMasterDataGateway;
import ddd.plus.showcase.wms.domain.task.Task;
import io.github.dddplus.model.spcification.AbstractSpecification;
import io.github.dddplus.model.spcification.Notification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 业务方明确提出：拣货人员不能自己完成复核任务，就像(出纳，会计)必须分离一样.
 */
@Slf4j
@AllArgsConstructor
public class OperatorCannotBePicker extends AbstractSpecification<Task> {
    private final IMasterDataGateway masterDataGateway;
    private final Operator candidate;

    @Override

    public boolean isSatisfiedBy(Task task, Notification notification) {
        if (!masterDataGateway.allowPerformChecking(candidate)) {
            notification.addError(WmsException.Code.OperatorDisallowed.getErrorCode());
            return false;
        }

        return true;
    }
}
