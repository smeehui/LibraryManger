package con.library.components.user.views;

import con.library.views.InputOption;
import con.library.views.View;

import java.util.Scanner;


public class UserMenuView extends View {
    public static void launch() {
        Scanner scanner = new Scanner(System.in);
        UserView userView = new UserView();
        int option;
        do {
            menuUser();
            option = tryInput.tryInt("lựa chọn");
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
                default:
                    System.out.println("Chọn chức năng không đúng! Vui lòng chọn lại");
                    break;
            }
        } while (tryInput.isReturn(String.valueOf(option)));
    }

    public static void menuUser() {
        System.out.println(tbConverter.convertMtplCol("USERS MANAGER", "Thêm người dùng", "Sửa thông tin người dùng", "Hiện danh sách người dùng "));
        ;
    }
}
