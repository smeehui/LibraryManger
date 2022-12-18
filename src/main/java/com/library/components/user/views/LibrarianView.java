package com.library.components.user.views;

import com.library.components.booklending.views.BookLendingMenu;
import com.library.components.fine.views.FineMenuView;
import com.library.views.View;
import com.library.components.books.views.BookItemMenuView;
import com.library.components.books.views.BookMenuView;
import com.library.views.ShowErrorMessage;

public class LibrarianView extends View {

    public static void menuOption(long id) {
        do {
            mainMenu();
            System.out.print("\nChọn chức năng ");
            System.out.print("⭆ ");
            int number = View.tryInput.tryInt("chức năng");
            if (number == -1) break;
            switch (number) {
                case 1 -> BookMenuView.launch();
                case 2 -> BookItemMenuView.launch(id);
                case 3 -> BookLendingMenu.launch();
                case 4 -> UserMenuView.launch();
                case 5 -> FineMenuView.launch();
                default -> ShowErrorMessage.outOfRange("chức năng");
            }
        } while (true);
    }


    public static void mainMenu() {
        System.out.println(View.tbConverter.convertMtplCol("MENU LIBRARIAN", "Quản lí sách", "Quản lí BookItem", "Quản lí BookLending", "Quản lí người dùng","Quản lý tiền phạt"));
    }

}
