package con.library.views;

public class ShowErrorMessage {
    public static void syntax(String field) {
        System.out.println("Nhập "+ field + " sai định dạng, vui lòng nhập lại!");
    }
    public static void outOfRange(String field) {
        System.out.println("Nhập "+ field + " không đúng, vui lòng nhập lại!");
    }
}
