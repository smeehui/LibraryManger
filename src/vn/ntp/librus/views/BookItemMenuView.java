package vn.ntp.librus.views;

import vn.ntp.librus.model.BookLending;
import vn.ntp.librus.utils.AppUtils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BookItemMenuView {
//    public static void main(String[] args) {
//        launch();
//    }

    public static void launch() {
        int choice;
        do {
            Scanner input = new Scanner(System.in);
            BookItemView bookItemView = new BookItemView();
            BookLendingView bookLendingView = new BookLendingView();
            menuBookItem();
            try {
                System.out.println("Chọn chức năng :");
                choice = AppUtils.retryParseInt();
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
                        System.out.println("Chọn chức năng không đúng. Vui lòng chọn lại");
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

        System.out.println("❄ ❄ ❄ ❄ ❄ ❄   MENU BOOKITEM  ❄ ❄ ❄ ❄ ❄ ❄");
        System.out.println("❄                                          ❄");
        System.out.println("❄        1. Chỉnh sửa BookItem             ❄");
        System.out.println("❄        2. Checkout BookItem              ❄");
        System.out.println("❄        3. Return Book                    ❄");
        System.out.println("❄        4. Hiển thị danh sách BookItem    ❄");
        System.out.println("❄        5. Quay lại Menu Librarian        ❄");
        System.out.println("❄        6. Exit                           ❄");
        System.out.println("❄                                          ❄");
        System.out.println("❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄");

    }
}
