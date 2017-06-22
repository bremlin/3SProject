package com.ibcon.factage_web.services.crud.user;

import com.ibcon.factage_web.domain.User;
import com.ibcon.factage_web.services.crud.CrudService;

public interface UserServiceCrud extends CrudService<User> {
    User findByUserName(String username);
}
