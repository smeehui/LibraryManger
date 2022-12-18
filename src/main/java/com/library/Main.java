package com.library;
import com.library.components.user.views.UserRegisterView;
import com.library.components.user.views.UserView;
import com.library.views.ShowErrorMessage;
import com.library.views.View;

public class Main extends View {
    public static void main(String[] args) {
        UserView userView = UserView.getInstance();
        UserRegisterView userRegisterView = UserRegisterView.getInstance();
        do {
            System.out.println(tbConverter.convertMtplCol("ĐĂNG NHẬP/ĐĂNG KÝ","Đăng nhập","Đăng ký"));
            int option = tryInput.tryInt("lựa chọn");
            switch (option) {
                case 1 -> userView.login();
                case 2 -> userRegisterView.launch();
                default -> ShowErrorMessage.outOfRange("lựa chọn");
            }
        }while (true);
    }

}
