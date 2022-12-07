package con.library.components.booklending.views;

import con.library.views.InputOption;
import con.library.components.user.views.LibrarianView;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BookLendingMenu {

    public static void menuBookLending() {
        System.out.println("❄ ❄ ❄ ❄ ❄   Menu BookLending   ❄ ❄ ❄ ❄ ❄");
        System.out.println("❄    1. Chỉnh sửa BookLending              ❄");
        System.out.println("❄    2. Hiển thị danh sách BookLending     ❄");
        System.out.println("❄    3. Quay lại Menu Librarian            ❄");
        System.out.println("❄    4. Exit                               ❄");
        System.out.println("❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄");
    }

    public static void launch() {
        int choice;
        do {
            Scanner scanner = new Scanner(System.in);
            BookLendingView bookLendingView = new BookLendingView();
            menuBookLending();
            try {
                System.out.println("Chọn chức năng: ");
                System.out.println("⭆");
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        bookLendingView.update();
                        break;
                    case 2:
                        bookLendingView.showBooksLending(InputOption.SHOW);
                        break;
                    case 3:
                        LibrarianView.menuOption();
                        break;

                    default:
                        System.out.println("Chọn chức năng không đúng. Vui lòng nhập lại.");
                }

            } catch (InputMismatchException io) {
                System.out.println("Nhập sai. Vui lòng nhập lại");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        } while (true);
    }

}
