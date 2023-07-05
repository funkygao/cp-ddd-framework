package ddd.plus.showcase.wms.domain.carton.dict;

public enum CartonStatus {
    Accepted,
    Full,
    Shipping,
    Shipped;

    public boolean isFull() {
        return Full.equals(this);
    }
}
