package com.ibcon.sproject.creators.project;

import com.ibcon.sproject.domain.Project;
import com.ibcon.sproject.domain.Role;

import java.util.List;

public class ProjectFormModelCreatorImpl extends AbstractProjectFormModelCreator {

    public ProjectFormModelCreatorImpl() {
    }

    public ProjectFormModelCreatorImpl(Project project, List<Role> allRoles) {
        this.project = project;
        this.allRoles = allRoles;
        fillRoleIds();
    }

    private void fillRoleIds() {
        project.getRoles().forEach(role -> roleIds.add(role.getId()));
    }
}
