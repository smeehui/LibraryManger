package con.library.components.user.views;

import con.library.views.InputOption;
import con.library.views.View;

import java.util.Scanner;


public class UserMenuView extends View {
    public static void launch() {
        Scanner scanner = new Scanner(System.in);
        UserView userView = new UserView();
        int option = -1;
        do {
            menuUser();
            try {
                do {
                    System.out.println("Chọn chức năng");
                    System.out.print(" ⭆ ");
                    option = Integer.parseInt(scanner.nextLine());
                    if (option > 4 || option < 1)
                        System.out.println("Chọn chức năng không đúng! Vui lòng chọn lại");
                } while (option > 4 || option < 1);

                switch (option) {
                    case 1:
                        userView.addUser();
                        break;
                    case 2:
                        userView.updateUser();
                        break;
                    case 3:
                        userView.showUsers(InputOption.SHOW);
                        break;
                    case 4:
                        LibrarianView.menuOption();
                        break;
                    default:
                        System.out.println("Chọn chức năng không đúng! Vui lòng chọn lại");
                        break;
                }
            } catch (Exception ex) {
                System.out.println("Nhập sai! vui lòng nhập lại");
            }
        } while (option != 4);
    }

    public static void menuUser() {
        tbConverter.convertMtplCol("USERS MANAGER", "Thêm người dùng", "Sửa thông tin người dùng", "Hiện danh sách người dùng ");
    }
}
