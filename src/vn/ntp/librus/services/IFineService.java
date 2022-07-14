package vn.ntp.librus.services;

import vn.ntp.librus.model.Fine;

import java.util.List;

public interface IFineService extends IAbstractService<Fine,Long> {

    void add(Fine newUser);

    void update(Fine newUser);

    Fine findById(long id);

    void collectFine(long userId, long days);
}
