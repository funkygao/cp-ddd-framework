package io.github.design;

import io.github.dddplus.dsl.KeyEvent;
import io.github.dddplus.model.IDomainModel;

/**
 * 拣货任务已完成.
 */
@KeyEvent(type = KeyEvent.Type.RemoteConsuming, remark = "hahadsf")
public class CheckTaskFinished implements IDomainModel {
}
