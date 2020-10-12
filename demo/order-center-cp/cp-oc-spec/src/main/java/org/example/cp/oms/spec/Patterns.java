package org.example.cp.oms.spec;

/**
 * 所有的业务模式，统一定义在此.
 */
public interface Patterns {

    /**
     * 海尔模式.
     */
    String Hair = "hair";

    /**
     * 家电模式.
     */
    String HomeAppliance = "home";

    /**
     * 冷链B2B模式.
     */
    String ColdChainB2B = "ccb2b";

    /**
     * 冷链B2C模式.
     */
    String ColdChainB2C = "ccb2c";

    /**
     * 业务模式标签在此统一定义.
     */
    interface Tags {
        String B2B = "B2B";
        String B2C = "B2C";
    }
}
