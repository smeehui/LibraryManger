package com.library.components.user.views;

import com.library.components.user.models.Role;
import com.library.views.InputOption;
import com.library.views.View;


public class UserMenuView extends View {
    public static void launch() {
        UserView userView = UserView.getInstance();
        int option;
        do {
            menuUser();
            option = tryInput.tryInt("lựa chọn");
            if (tryInput.isReturn(String.valueOf(option))) break;
            switch (option) {
                case 1 -> userView.addUser(Role.LIBRARIAN);
                case 2 -> userView.updateUserView();
                case 3 -> userView.showUsers(InputOption.SHOW);
                default -> System.out.println("Chọn chức năng không đúng! Vui lòng chọn lại");
            }
        } while (true);
    }

    public static void menuUser() {
        System.out.println(tbConverter.convertMtplCol("USERS MANAGER", "Thêm người dùng", "Sửa thông tin người dùng", "Hiện danh sách người dùng "));
    }
}
