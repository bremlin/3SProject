package com.ibcon.sproject.creators.user;

import com.ibcon.sproject.domain.Role;
import com.ibcon.sproject.domain.User;
import com.ibcon.sproject.services.crud.role.RoleService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractUserFormCreator implements UserFormModelCreator {
    protected User user = new User();
    protected List<Role> allRoles = new ArrayList<>();
    protected List<Integer> roleIds = new ArrayList<>();

    @Override
    public User createUser(RoleService roleService) {
        user.setRoles(new HashSet<>());
        for (Integer roleId : roleIds) {
            user.addRole(roleService.getById(roleId));
        }
        return user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Role> getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(List<Role> allRoles) {
        this.allRoles = allRoles;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }
}
