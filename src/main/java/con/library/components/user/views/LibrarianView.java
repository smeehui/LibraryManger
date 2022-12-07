package con.library.components.user.views;

import con.library.components.booklending.views.BookLendingMenu;
import con.library.components.books.views.BookItemMenuView;
import con.library.components.books.views.BookMenuView;
import con.library.views.MenuView;
import con.library.views.ShowErrorMessage;
import con.library.views.View;

import java.util.InputMismatchException;

public class LibrarianView extends UserView {

    public static void menuOption() {
        do {
            mainMenu();
            System.out.println("\nChọn chức năng ");
            System.out.print("⭆ ");
            int number = tryInput.tryInt("chức năng");
            if (number == -1) break;
            switch (number) {
                case 1 -> BookMenuView.launch();
                case 2 -> BookItemMenuView.launch();
                case 3 -> BookLendingMenu.launch();
                case 4 -> UserMenuView.launch();
                default -> {
                    ShowErrorMessage.outOfRange("chức năng");
                }
            }
        } while (true);
    }


    public static void mainMenu() {
        System.out.println(View.tbConverter.convertMtplCol("MENU LIBRARIAN", "Quản lí sách", "Quản lí BookItem", "Quản lí BookLending", "Quản lí người dùng "));
    }

}
