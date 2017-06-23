package com.ibcon.sproject.creators.user;

import com.ibcon.sproject.domain.User;
import com.ibcon.sproject.services.crud.role.RoleService;

public interface UserFormModelCreator {
    User createUser(RoleService roleService);
}
