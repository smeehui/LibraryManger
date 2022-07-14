package vn.ntp.librus.services;

import java.util.List;

public interface IAbstractService <Model,ID>{
    abstract List<Model> findAll();

    Model findById(ID id);

    boolean existById(ID id);

}
