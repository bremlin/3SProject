package com.ibcon.sproject.controllers;

import com.ibcon.sproject.creators.project.EmptyProjectFormCreatorImpl;
import com.ibcon.sproject.creators.project.ProjectFormModelCreator;
import com.ibcon.sproject.creators.project.ProjectFormModelCreatorImpl;
import com.ibcon.sproject.domain.Project;
import com.ibcon.sproject.domain.Role;
import com.ibcon.sproject.services.crud.project.ProjectService;
import com.ibcon.sproject.services.crud.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class ProjectController {
    ProjectService projectService;
    RoleService roleService;

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public String listAllProjects(Model model) {
        model.addAttribute("projects", projectService.listAll());
        return "projects";
    }
    @RequestMapping("project/new")
    public String create(Model model) {
        ProjectFormModelCreator emptyProjectFormCreator = new EmptyProjectFormCreatorImpl(roleService.listAll());
        model.addAttribute("project", emptyProjectFormCreator);
        return "projectform";
    }

    @RequestMapping(value = "project", method = RequestMethod.POST)
    public String save(@ModelAttribute("project") ProjectFormModelCreatorImpl projectFormModelCreator) {
        Project project = projectFormModelCreator.createProject(roleService);
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
        ProjectFormModelCreator ProjectFormCreator = new ProjectFormModelCreatorImpl(projectService.getById(id), (List<Role>) roleService.listAll());
        model.addAttribute("project", ProjectFormCreator);
        return "projectform";
    }

    @RequestMapping("project/delete/{id}")
    public String delete(@PathVariable Integer id) {
        projectService.delete(id);
        return "redirect:/projects";
    }
}
