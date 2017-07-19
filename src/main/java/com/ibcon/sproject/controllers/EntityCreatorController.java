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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class EntityCreatorController {
    private UserServiceCrud userServiceCrud;
    private RoleService roleService;
    private ProjectService projectService;
    private WBSService wbsService;

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
    public ResponseEntity<?> createWBS(@Valid @RequestBody WBS wbs, Errors errors) {
        String result = "";

        if (errors.hasErrors()) {

            result.join(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }

        com.ibcon.sproject.domain.WBS wbsEntity = new com.ibcon.sproject.domain.WBS();
        //TODO лишнее?
        Project project = projectService.getById(wbs.getProjectId());

        if (wbs.getId() != null) {
            wbsEntity = wbsService.getById(wbs.getId());
        }
        wbsEntity.setName(wbs.getName());
        wbsEntity.setCode(wbs.getCode());
        wbsEntity.setProject(project);
        wbsEntity.setProjectObjectId(project.getProjectObjectId());
        wbsEntity.setParentId(0);
        wbsEntity.setIndexNumber(1);

        result = String.valueOf(wbsService.saveOrUpdate(wbsEntity).getId());

        return ResponseEntity.ok(result);
    }

    //TODO USE JSON
    //TODO какой маппинг?
    @GetMapping(value = "/edit_wbs", produces = "application/json")
    public ResponseEntity<?> editWBS(@RequestParam("id") String str) {
        com.ibcon.sproject.domain.WBS wbs = wbsService.getById(Integer.valueOf(str));

        WBS wbsResult = new WBS();
        wbsResult.setId(Integer.valueOf(str));
        wbsResult.setProjectId(wbs.getProject().getId());
        wbsResult.setName(wbs.getName());
        wbsResult.setCode(wbs.getCode());
        return ResponseEntity.ok(wbsResult);
    }
}
