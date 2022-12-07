package con.library.components.books.views;

import con.library.utils.AppUtils;
import con.library.components.booklending.views.BookLendingView;
import con.library.views.InputOption;
import con.library.components.user.views.LibrarianView;
import con.library.views.MenuView;
import con.library.views.ShowErrorMessage;
import con.library.views.View;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BookItemMenuView  extends View {

    public static void launch() {
        int choice;
        do {
            BookItemView bookItemView = new BookItemView();
            BookLendingView bookLendingView = new BookLendingView();
            menuBookItem();
            try {
                System.out.println("Chọn chức năng :");
                choice = tryInput.tryInt("Lựa chọn");
                switch (choice) {
                    case 1:
                        bookItemView.update();
                        break;
                    case 2:
                        bookLendingView.checkoutBook();
                        break;
                    case 3:
                        bookLendingView.returnBook();
                        break;
                    case 4:
                        bookItemView.showBooksItem(InputOption.SHOW);
                        break;
                    case 5:
                        LibrarianView.menuOption();
                        break;
                    case 6:
                        MenuView.exit();
                        break;
                    default:
                        ShowErrorMessage.outOfRange("Lựa chọn");
                }

            } catch (InputMismatchException io) {
                System.out.println("Nhập sai. Vui lòng nhập lại");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        } while (true);
    }

    public static void menuBookItem() {
        System.out.println(tbConverter.convertMtplCol("MENU BOOKITEM","Chỉnh sửa BookItem",
                                                                      "Checkout BookItem",
                                                                      "Return Book",
                                                                      "Hiển thị danh sách BookItem",
                                                                      "Quay lại Menu Librarian"));

//        System.out.println("❄ ❄ ❄ ❄ ❄ ❄   MENU BOOKITEM  ❄ ❄ ❄ ❄ ❄ ❄");
//        System.out.println("❄                                          ❄");
//        System.out.println("❄        1. Chỉnh sửa BookItem             ❄");
//        System.out.println("❄        2. Checkout BookItem              ❄");
//        System.out.println("❄        3. Return Book                    ❄");
//        System.out.println("❄        4. Hiển thị danh sách BookItem    ❄");
//        System.out.println("❄        5. Quay lại Menu Librarian        ❄");
//        System.out.println("❄        6. Exit                           ❄");
//        System.out.println("❄                                          ❄");
//        System.out.println("❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄");

    }
}
