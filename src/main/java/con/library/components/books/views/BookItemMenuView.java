package con.library.components.books.views;

import con.library.components.booklending.views.BookLendingView;
import con.library.views.InputOption;
import con.library.views.ShowErrorMessage;
import con.library.views.View;

public class BookItemMenuView extends View {

    public static void launch() {
        int choice;
        do {
            BookItemView bookItemView = new BookItemView();
            BookLendingView bookLendingView = new BookLendingView();
            menuBookItem();
            System.out.println("Chọn chức năng :");
            choice = tryInput.tryInt("Lựa chọn");
            if (tryInput.isReturn(String.valueOf(choice))) break;
            switch (choice) {
                case 1 -> bookItemView.update();
                case 2 -> bookLendingView.checkoutBook();
                case 3 -> bookLendingView.returnBook();
                case 4 -> bookItemView.showBooksItem(InputOption.SHOW);
                default -> ShowErrorMessage.outOfRange("Lựa chọn");
            }
        } while (true);
    }

    public static void menuBookItem() {
        System.out.println(tbConverter.convertMtplCol("MENU SÁCH MƯỢN", "Chỉnh sửa sách mượn",
                "Mượn sách",
                "Trả sách",
                "Hiển thị danh sách sách mượn"));
    }
}
