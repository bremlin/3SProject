package com.ibcon.sproject.services.crud.project;

import com.ibcon.sproject.domain.Project;
import com.ibcon.sproject.services.crud.CrudService;

public interface ProjectService extends CrudService<Project> {
    Project findByProjectObjectId(Integer projectObjectId);
}
