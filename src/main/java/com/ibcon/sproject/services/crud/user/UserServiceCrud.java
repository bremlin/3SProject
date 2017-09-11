package com.ibcon.sproject.services.crud.user;

import com.ibcon.sproject.creators.user.UserFormModelCreator;
import com.ibcon.sproject.domain.User;
import com.ibcon.sproject.services.crud.CrudService;

public interface UserServiceCrud extends CrudService<User> {
    User findByUserName(String username);
    User saveOrUpdate(UserFormModelCreator userFormModelCreator);
}
