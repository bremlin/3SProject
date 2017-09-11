package com.ibcon.sproject.repositories;

import com.ibcon.sproject.domain.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Integer> {
    Project findByProjectObjectId(Integer projectObjectId);
}
