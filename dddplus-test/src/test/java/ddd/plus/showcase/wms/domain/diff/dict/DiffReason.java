package ddd.plus.showcase.wms.domain.diff.dict;

public enum  DiffReason {
    Broken,
    Lost;

    public boolean isBroken() {
        return Broken.equals(this);
    }

    public boolean isLost() {
        return Lost.equals(this);
    }
}
