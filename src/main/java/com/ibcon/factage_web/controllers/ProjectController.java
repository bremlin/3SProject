package com.ibcon.factage_web.controllers;

import com.ibcon.factage_web.domain.Project;
import com.ibcon.factage_web.services.crud.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProjectController {
    ProjectService projectService;

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public String listAllProjects(Model model) {
        model.addAttribute("projects", projectService.listAll());
        return "projects";
    }
    @RequestMapping("project/new")
    public String create(Model model) {
        model.addAttribute("project", new Project());
        return "projectform";
    }

    @RequestMapping(value = "project", method = RequestMethod.POST)
    public String save(Project project) {
        projectService.saveOrUpdate(project);
        return "redirect:/project/" + project.getId();
    }

    @RequestMapping("project/{id}")
    public String showUser(@PathVariable Integer id, Model model) {
        model.addAttribute("project", projectService.getById(id));
        return "projectshow";
    }

    @RequestMapping("project/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("project", projectService.getById(id));
        return "projectform";
    }

    @RequestMapping("project/delete/{id}")
    public String delete(@PathVariable Integer id) {
        projectService.delete(id);
        return "redirect:/projects";
    }
}
