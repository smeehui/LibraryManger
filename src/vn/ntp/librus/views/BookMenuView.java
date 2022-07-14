package vn.ntp.librus.views;

import java.util.InputMismatchException;
import java.util.Scanner;

public class BookMenuView {

    public static void launch() {
        int choice;
        do {
            Scanner input = new Scanner(System.in);
            BookView bookView = new BookView();
            menuBook();
            try {
                System.out.println("Chọn chức năng :");
                System.out.println(" ⭆");
                choice = input.nextInt();
                switch (choice) {
                    case 1:
                        bookView.add();
                        break;
                    case 2:
                        bookView.update();
                        break;
                    case 3:
                        bookView.remove();
                        break;
                    case 4:
                        bookView.showBooks(InputOption.SHOW);
                        break;
                    case 5:
                        bookView.findBooksByName();
                        break;
                    case 6:
                        LibrarianView.menuOption();
                        break;
                    case 7:
                        System.exit(0);
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

    public static void menuBook() {
        System.out.println("❄ ❄ ❄ ❄ ❄ ❄ ❄  MENU BOOK  ❄ ❄ ❄ ❄ ❄ ❄ ❄");
        System.out.println("❄        1. Thêm sách                      ❄");
        System.out.println("❄        2. Chỉnh sửa sách                 ❄");
        System.out.println("❄        3. Xóa sách                       ❄");
        System.out.println("❄        4. Hiển thị danh sách sách        ❄");
        System.out.println("❄        5. Tìm kiếm sách theo tên         ❄");
        System.out.println("❄        6. Quay lại Menu Librarian        ❄");
        System.out.println("❄        7. Exit                           ❄");
        System.out.println("❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄ ❄");

    }
}
