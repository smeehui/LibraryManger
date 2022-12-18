package com.library.components.books.views;

import com.library.components.booklending.views.BookLendingView;
import com.library.views.InputOption;
import com.library.views.View;
import com.library.views.ShowErrorMessage;

public class BookItemMenuView extends View {

    public static void launch(long id) {
        int choice;
        do {
            BookItemView bookItemView = new BookItemView();
            BookLendingView bookLendingView = BookLendingView.getInstance();
            menuBookItem();
            System.out.println("Chọn chức năng :");
            choice = tryInput.tryInt("Lựa chọn");
            if (tryInput.isReturn(String.valueOf(choice))) break;
            switch (choice) {
                case 1 -> bookItemView.update();
                case 2 -> bookLendingView.checkoutBook(id);
                case 3 -> bookLendingView.returnBook(id);
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
