package vn.ntp.librus.model;

public enum BookFormat {
    PAPERBACK("PAPERBACK"),
    HARDCOVER("HARDCOVER"),
    NEWSPAPER("NEWSPAPER"),
    MAGAZINE("MAGAZINE"),
    EBOOK("EBOOK");

    private final String value;

    private BookFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static BookFormat parserBookFormat(String value) {
        BookFormat[] values = values();
        for (BookFormat bookFormat : values) {
            if (bookFormat.value.equals(value))
                return bookFormat;
        }

        throw new IllegalArgumentException("Invalid BookFormat value : " + value);

    }
}
