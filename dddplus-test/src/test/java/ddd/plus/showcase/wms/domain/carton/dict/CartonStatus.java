package ddd.plus.showcase.wms.domain.carton.dict;

public enum CartonStatus {
    Accepted,
    Full;

    public boolean isFull() {
        return Full.equals(this);
    }
}
