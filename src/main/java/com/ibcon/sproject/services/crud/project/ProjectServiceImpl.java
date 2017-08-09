package com.ibcon.sproject.services.crud.project;

import com.ibcon.sproject.domain.Project;
import com.ibcon.sproject.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository projectRepository;

    @Autowired
    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<?> listAll() {
        List<Project> projects = new ArrayList<>();
        projectRepository.findAll().forEach(projects::add);
        return projects;
    }

    @Override
    public Project getById(Integer id) {
        return projectRepository.findOne(id);
    }

    @Override
    public Project saveOrUpdate(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public void delete(Integer id) {
        projectRepository.delete(id);
    }

    @Override
    public Project findByProjectObjectId(Integer projectObjectId) {
        return projectRepository.findByProjectObjectId(projectObjectId);
    }
}
