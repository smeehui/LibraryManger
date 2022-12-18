package com.library.components.user.services;

import com.library.services.IAbstractService;
import com.library.components.user.models.User;

public interface IUserService extends IAbstractService<User,Long> {

    public User login(String username, String password);

    void add(User newUser);

    void update(User newUser);

    boolean existById(long id);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByUsername(String userName);

    void blockMember(long id);

    void unBlockMember(long id);


}
