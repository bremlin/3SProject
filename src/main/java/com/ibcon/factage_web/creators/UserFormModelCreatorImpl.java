package com.ibcon.factage_web.creators;

import com.ibcon.factage_web.domain.Role;
import com.ibcon.factage_web.domain.User;
import com.ibcon.factage_web.services.crud.role.RoleService;

import java.util.ArrayList;
import java.util.List;

public class UserFormModelCreatorImpl implements UserFormModelCreator {
    private User user = new User();
    private List<Role> allRoles;
    private List<Integer> roleIds = new ArrayList<>();

    public UserFormModelCreatorImpl() {
    }

    public UserFormModelCreatorImpl(List<?> objects) {
        this.allRoles = (List<Role>) objects;
    }

    @Override
    public User createUser(RoleService roleService) {
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
