package com.ibcon.factage_web.services.crud.project;

import com.ibcon.factage_web.domain.Project;
import com.ibcon.factage_web.services.crud.CrudService;

public interface ProjectService extends CrudService<Project> {
    Project findByProjectObjectId(Integer projectObjectId);
}
