package org.example.cp.oms.domain;

import io.github.dddplus.annotation.Domain;

@Domain(code = CoreDomain.CODE, name = "订单核心域")
public class CoreDomain {
    public static final String CODE = "core";

    private static final ApplicationLifeCycle applicationLifeCycle = new ApplicationLifeCycle();

    /**
     * 获取应用生命周期状态.
     */
    public static ApplicationLifeCycle getApplicationLifeCycle() {
        return applicationLifeCycle;
    }

    public static final class ApplicationLifeCycle {

        /**
         * 应用当前状态
         */
        private State state = State.INITIALIZED;

        public enum State {
            /**
             * Initialized but not yet started.
             */
            INITIALIZED,

            /**
             * In the process of starting.
             */
            STARTING,

            /**
             * Has started.
             */
            STARTED,

            /**
             * Stopping is in progress.
             */
            STOPPING,

            /**
             * Has stopped.
             */
            STOPPED
        }

        /**
         * 启动应用完毕
         */
        public void started() {
            state = State.STARTED;
        }

        /**
         * 开始停止应用
         */
        public void stop() {
            state = State.STOPPING;
        }

        /**
         * 停止应用完毕
         */
        public void stopped() {
            state = State.STOPPED;
        }

        /**
         * 是否停止中
         *
         * @return true or false
         */
        public boolean isStopping() {
            return state == State.STOPPING;
        }

        /**
         * 是否通知完毕
         *
         * @return true or false
         */
        public boolean isStopped() {
            return state == State.STOPPED;
        }
    }

}
