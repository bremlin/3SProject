package com.ibcon.sproject.creators.project;

import com.ibcon.sproject.domain.Project;
import com.ibcon.sproject.domain.Role;
import com.ibcon.sproject.services.crud.role.RoleService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AbstractProjectFormModelCreator implements ProjectFormModelCreator {
    protected Project project = new Project();
    protected List<Role> allRoles = new ArrayList<>();
    protected List<Integer> roleIds = new ArrayList<>();

    @Override
    public Project createProject(RoleService roleService) {
        project.setRoles(new HashSet<>());
        for (Integer roleId : roleIds) {
            project.addRole(roleService.getById(roleId));
        }
        return project;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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
