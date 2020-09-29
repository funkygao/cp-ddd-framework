package org.example.cp.oms.spec.model.vo;

import java.util.List;

public interface IProductDelegate {
    List<? extends IProduct> getProducts();
}
