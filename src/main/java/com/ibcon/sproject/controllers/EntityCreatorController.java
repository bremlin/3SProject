package com.ibcon.sproject.controllers;

import com.ibcon.sproject.controllers.MainView.dto.WBS;
import com.ibcon.sproject.domain.Project;
import com.ibcon.sproject.services.crud.project.ProjectService;
import com.ibcon.sproject.services.crud.role.RoleService;
import com.ibcon.sproject.services.crud.user.UserServiceCrud;
import com.ibcon.sproject.services.crud.wbs.WBSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class EntityCreatorController {
    UserServiceCrud userServiceCrud;
    RoleService roleService;
    ProjectService projectService;
    WBSService wbsService;

    @Autowired
    public void setUserServiceCrud(UserServiceCrud userServiceCrud) {
        this.userServiceCrud = userServiceCrud;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Autowired
    public void setWbsService(WBSService wbsService) {
        this.wbsService = wbsService;
    }

    @PostMapping("/add_wbs")
    public ResponseEntity<?> createWBS(
            @Valid @RequestBody WBS wbs, Errors errors) {
        String result = "";

        if (errors.hasErrors()) {

            result.join(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }

        com.ibcon.sproject.domain.WBS wbsEntity = new com.ibcon.sproject.domain.WBS();
        Project project = projectService.getById(wbs.getProjectId());
        wbsEntity.setName(wbs.getName());
        wbsEntity.setCode(wbs.getCode());
        wbsEntity.setProject(project);
        wbsEntity.setProjectObjectId(project.getProjectObjectId());
        wbsEntity.setParentId(0);
        wbsEntity.setIndexNumber(1);

        result = String.valueOf(wbsService.saveOrUpdate(wbsEntity).getId());

        return ResponseEntity.ok(result);
    }
}
