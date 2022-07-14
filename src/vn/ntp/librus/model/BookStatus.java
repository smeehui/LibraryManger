package vn.ntp.librus.model;

public enum BookStatus {
    AVAILABLE("AVAILABLE"),
    LOANED("LOANED"),
    LOST("LOST");

    private final String value;

    private BookStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static BookStatus parseBookStatus(String value) {
        BookStatus[] values = values();
        for (BookStatus bookStatus : values) {
            if (bookStatus.value.equals(value))
                return bookStatus;
        }

        throw new IllegalArgumentException("Invalid BookStatus value : " + value);
    }
}

