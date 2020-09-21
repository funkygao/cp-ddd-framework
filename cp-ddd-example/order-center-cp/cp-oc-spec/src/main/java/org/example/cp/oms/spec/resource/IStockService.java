package org.example.cp.oms.spec.resource;

public interface IStockService {

    /**
     * 调用库存中心进行预占库存的操作.
     *
     * @param sku SKU number
     * @return true if successful
     */
    boolean preOccupyStock(String sku);
}
