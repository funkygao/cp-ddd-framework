package org.example.cp.oms.spec.ext;

import org.cdf.ddd.ext.IDomainExtension;
import org.example.cp.oms.spec.model.IOrderModel;

import javax.validation.constraints.NotNull;

/**
 * 敏感词信息获取.
 */
public interface ISensitiveWordsExt extends IDomainExtension {

    String[] extract(@NotNull IOrderModel model);
}
