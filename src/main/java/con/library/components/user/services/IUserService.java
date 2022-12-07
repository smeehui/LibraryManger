package con.library.components.user.services;

import con.library.components.user.models.User;
import con.library.services.IAbstractService;

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
