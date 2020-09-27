package org.example.cp.oms.spec.exception;

import javax.validation.constraints.NotNull;

public class OrderException extends RuntimeException {
    protected OrderErrorSpec errorReason;

    /**
     * (业务前台)个性化消息.
     * <p>
     * <p>例如，业务前台要求它抛出的错误消息，中台不要再加工，要原封不动地输出</p>
     */
    protected String custom;

    public OrderException(@NotNull OrderErrorSpec errorReason) {
        super();
        this.errorReason = errorReason;
    }

    /**
     * 设置(业务前台)个性化消息.
     *
     * @param custom 个性化消息
     */
    public OrderException withCustom(String custom) {
        this.custom = custom;
        return this;
    }

    public String getCustom() {
        return custom;
    }

    /**
     * 是否有个性化信息.
     */
    public boolean hasCustom() {
        return custom != null;
    }

    public String code() {
        return errorReason.code();
    }

    @Override
    public String getMessage() {
        if (hasCustom()) {
            return custom;
        }

        return code();
    }

}
