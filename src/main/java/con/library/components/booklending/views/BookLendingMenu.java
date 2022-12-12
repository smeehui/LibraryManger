package con.library.components.booklending.views;

import con.library.views.InputOption;
import con.library.views.ShowErrorMessage;
import con.library.views.View;

public class BookLendingMenu extends View {

    public static void menuBookLending() {
        System.out.println(tbConverter.convertMtplCol("QUẢN LÝ MƯỢN/TRẢ SÁCH", "Chỉnh sửa BookLending", "Hiển thị danh sách BookLending"));
    }

    public static void launch() {
        int choice;
        do {
            BookLendingView bookLendingView = new BookLendingView();
            menuBookLending();
            System.out.println("Chọn chức năng: ");
            System.out.print("⭆\t");
            choice = tryInput.tryInt("chức năng");
            if (tryInput.isReturn(String.valueOf(choice))) break;
            switch (choice) {
                case 1 -> bookLendingView.update();
                case 2 -> bookLendingView.showBooksLending(InputOption.SHOW);
                default -> ShowErrorMessage.outOfRange("lựa chọn");
            }
        } while (true);
    }

}
