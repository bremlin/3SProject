package com.ibcon.factage_web.repositories;

import com.ibcon.factage_web.domain.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Integer> {
    Project findByProjectObjectId(Integer projectObjectId);
}
