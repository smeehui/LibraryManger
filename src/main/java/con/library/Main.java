package con.library;

import con.library.components.user.views.LibrarianView;
import con.library.components.user.views.UserView;

public class Main {
    public static void main(String[] args) {
        UserView userView = new UserView();
//        userView.login();
        LibrarianView.menuOption();
    }

}
