package com.library.components.user.views;

import com.library.components.user.models.Role;

public class UserRegisterView extends UserView{
    private static UserRegisterView instance;
    private UserRegisterView(){
    }
    public static UserRegisterView getInstance(){
        if (instance == null)instance = new UserRegisterView();
        return instance;
    }
    public void launch() {
        addUser(Role.MEMBER);
    }
}
