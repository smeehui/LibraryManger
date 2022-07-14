package vn.ntp.librus.model;

public enum Role {
    LIBRARIAN("LIBRARIAN"),
    MEMBER("MEMBER");
    private final String value;

    private Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static Role parseRole(String value) {
        for (Role role : values()) {
            if (role.value.equals(value))
                return role;
        }

        throw new IllegalArgumentException("Invalid role value : " + value);
    }

}
