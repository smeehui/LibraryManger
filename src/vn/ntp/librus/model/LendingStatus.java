package vn.ntp.librus.model;

public enum LendingStatus {
    LENDING("LENDING"),
    DUE("DUE"),
    RETURN("RETURN");

    private final String value;

    private LendingStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static LendingStatus parseLendingStatus(String value) {
        for (LendingStatus status : values()) {
            if (status.value.equals(value))
                return status;
        }

        throw new IllegalArgumentException("Invalid lending status : " + value);
    }
}
