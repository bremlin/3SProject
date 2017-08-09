package com.ibcon.sproject.creators.project;

import com.ibcon.sproject.domain.Project;
import com.ibcon.sproject.services.crud.role.RoleService;

public interface ProjectFormModelCreator {
    Project createProject(RoleService roleService);
}
