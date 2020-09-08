package org.cp.oms.domain.model.vo;

import org.cp.oms.domain.model.OrderModelCreator;
import org.cp.oms.spec.model.vo.IProduct;
import org.cp.oms.spec.model.vo.IProductDelegate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ProductDelegate implements IProductDelegate {

    private List<Product> products;

    private ProductDelegate() {}

    public static ProductDelegate createWith(@NotNull OrderModelCreator creator) {
        ProductDelegate delegate = new ProductDelegate();
        delegate.products = new ArrayList<>();
        return delegate;
    }


    @Override
    public List<? extends IProduct> getProducts() {
        return products;
    }
}
