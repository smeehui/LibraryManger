package con.library.components.fine.services;

import con.library.components.fine.models.Fine;
import con.library.services.IAbstractService;

public interface IFineService extends IAbstractService<Fine,Long> {

    void add(Fine newUser);

    void update(Fine newUser);

    Fine findById(long id);

    void collectFine(long userId, long days);
}
