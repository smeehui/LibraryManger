package com.library.components.fine.services;

import com.library.components.fine.models.FineStatus;
import com.library.services.IAbstractService;
import com.library.components.fine.models.Fine;

import java.util.List;

public interface IFineService extends IAbstractService<Fine,Long> {

    void add(Fine newUser);

    void update(Fine newUser);

    Fine findById(long id);

    void collectFine();
     Fine findByUserIdAndBookItemId(long userId, long bookItemId);
     List<Fine> findUnpaidFines ();

    List<Fine> findByUserId(long id);
    boolean isExistByUserId(long userId);

    List<Fine> findByUserIdAndStatus(long id, FineStatus status);
}
