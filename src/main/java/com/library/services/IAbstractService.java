package com.library.services;

import java.util.List;

public interface IAbstractService<Model, ID> {
    List<Model> findAll();

    Model findById(ID id);

    boolean existsById(ID id);

    void add(Model newEntity);

    void update(Model newEntity);

    public abstract void deleteById(Long id);
}
