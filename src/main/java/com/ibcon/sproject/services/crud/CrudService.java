package com.ibcon.sproject.services.crud;

import java.util.List;

public interface CrudService<T> {
    List<?> listAll();

    T getById(Integer id);

    T saveOrUpdate(T domainObject);

    void delete(Integer id);
}
