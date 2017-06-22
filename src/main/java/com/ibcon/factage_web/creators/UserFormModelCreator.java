package com.ibcon.factage_web.creators;

import com.ibcon.factage_web.domain.User;
import com.ibcon.factage_web.services.crud.role.RoleService;

public interface UserFormModelCreator {
    User createUser(RoleService roleService);
}
