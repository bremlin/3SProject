package com.ibcon.sproject.creators.user;

import com.ibcon.sproject.domain.Role;
import com.ibcon.sproject.domain.User;
import com.ibcon.sproject.services.crud.role.RoleService;
import com.ibcon.sproject.services.crud.user.UserServiceCrud;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFormModelCreatorImpl extends AbstractUserFormCreator {

    public UserFormModelCreatorImpl() {
    }

    public UserFormModelCreatorImpl(User user, List<Role> allRoles) {
        this.user = user;
        this.allRoles = allRoles;
        fillRoleIds();
    }

    private void fillRoleIds() {
        user.getRoles().forEach(role -> roleIds.add(role.getId()));
    }

    public User createUser(RoleService roleService, UserServiceCrud userServiceCrud) {
        User user = super.createUser(roleService);
        if (user.getId() != null) {
            String encryptedPassword = userServiceCrud.getById(user.getId()).getEncryptedPassword();
            user.setEncryptedPassword(encryptedPassword);
        }
        return user;
    }
}
