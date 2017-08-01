package com.ibcon.sproject.controllers;

import com.ibcon.sproject.controllers.mainview.dto.Activity;
import com.ibcon.sproject.controllers.mainview.dto.Id;
import com.ibcon.sproject.controllers.mainview.dto.WBS;
import com.ibcon.sproject.domain.Link;
import com.ibcon.sproject.domain.Project;
import com.ibcon.sproject.repositories.LinkRepository;
import com.ibcon.sproject.services.crud.activity.ActivityService;
import com.ibcon.sproject.services.crud.project.ProjectService;
import com.ibcon.sproject.services.crud.role.RoleService;
import com.ibcon.sproject.services.crud.user.UserServiceCrud;
import com.ibcon.sproject.services.crud.wbs.WBSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EntityCreatorController {
    private UserServiceCrud userServiceCrud;
    private RoleService roleService;
    private ProjectService projectService;
    private WBSService wbsService;
    private ActivityService activityService;
    private LinkRepository linkRepository;

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

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Autowired
    public void setLinkRepository(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
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

    @PostMapping("/delete_wbs")
    public ResponseEntity<?> deleteWBS(@Valid @RequestBody Id id, Errors errors) {
        id.getIdList().forEach(wbsService::delete);
//        wbsService.delete(id.getId());
        return ResponseEntity.ok("");
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

    //TODO not finished
    @PostMapping("/add_act")
    public ResponseEntity<?> createAct(@Valid @RequestBody Activity activity, Errors errors) {
        String result = "";

        List<Link> link = linkRepository.findAllByActivity_Id(activity.getActId());

        if (errors.hasErrors()) {

            result.join(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }

        com.ibcon.sproject.domain.Activity activityEntity= new com.ibcon.sproject.domain.Activity();
        com.ibcon.sproject.domain.WBS wbs = wbsService.getById(activity.getWbsId());

        if (activity.getActId() != null) {
            activityEntity = activityService.getById(activity.getActId());
        }
        activityEntity.setActivityName(activity.getName());
//        activityEntity.setCost(activity.getCost());
        activityEntity.setWbs(wbs);
        activityEntity.setProjectObjectId(wbs.getProject().getProjectObjectId());
        activityEntity.setProjectId(wbs.getProject().getId());

        //TODO populate
        activityEntity.setActivityId("act_id");
        activityEntity.setActivityName(activity.getName());
        activityEntity.setTypeId(1);
        activityEntity.setStatusId(1);
        activityEntity.setPercentageCompletion(0);
        activityEntity.setIsCritical(false);

        result = String.valueOf(activityService.saveOrUpdate(activityEntity).getId());

        return ResponseEntity.ok(result);
    }
}
