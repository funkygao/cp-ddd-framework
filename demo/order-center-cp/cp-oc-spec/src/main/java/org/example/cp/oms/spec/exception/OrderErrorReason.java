package org.example.cp.oms.spec.exception;

public interface OrderErrorReason {

    enum SubmitOrder implements OrderErrorSpec {
        OrderConcurrentNotAllowed("101", "同一个订单不允许并发"),
        InvalidExtenalNo("102", "非法的外部单号"),
        ;

        private final String code;
        private final String message;

        SubmitOrder(String code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public String code() {
            return code;
        }
    }

    enum CancelOrder implements OrderErrorSpec {
        OrderConcurrentNotAllowed("201", "同一个订单不允许并发"),
        ;

        private final String code;
        private final String message;

        CancelOrder(String code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public String code() {
            return code;
        }
    }

    enum Custom implements OrderErrorSpec {
        Custom("999", "前台自定义"),
        ;

        private final String code;
        private final String message;

        Custom(String code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public String code() {
            return code;
        }
    }
}
