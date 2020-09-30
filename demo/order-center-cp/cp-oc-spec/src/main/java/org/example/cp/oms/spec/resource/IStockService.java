package org.example.cp.oms.spec.resource;

// 这是个RPC资源的示例
// Redis、JDBC、MQ等资源，同理：spec jar里声明，infrastructure jar里封装实现
// 这样，Plugin所有的外部依赖都通过resource package由中台输出
public interface IStockService {

    /**
     * 调用库存中心进行预占库存的操作.
     *
     * @param sku SKU number
     * @return true if successful
     */
    boolean preOccupyStock(String sku);
}
