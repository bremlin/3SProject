package com.ibcon.sproject.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.ibcon.sproject.converters.smettojson.SmetJsonTree;
import com.ibcon.sproject.converters.smettojson.SmetTypeJson;
import com.ibcon.sproject.converters.wbstojson.WBSJson;
import com.ibcon.sproject.converters.wbstojson.WBSJSONTree;
import com.ibcon.sproject.domain.WBS;
import com.ibcon.sproject.domain.mixins.WBSInnerObjectsMixin;
import com.ibcon.sproject.services.crud.activity.ActivityService;
import com.ibcon.sproject.services.crud.estimatesmet.EstimateSmetService;
import com.ibcon.sproject.services.crud.project.ProjectService;
import com.ibcon.sproject.services.crud.role.RoleService;
import com.ibcon.sproject.services.crud.user.UserServiceCrud;
import com.ibcon.sproject.services.crud.wbs.WBSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestTableController {
    private UserServiceCrud userServiceCrud;
    private RoleService roleService;
    private ProjectService projectService;
    private WBSService wbsService;
    private ActivityService activityService;
    private EstimateSmetService estimateSmetService;

    private WBSJSONTree wbsJsonTree;
    private SmetJsonTree smetJsonTree;

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
    public void setEstimateSmetService(EstimateSmetService estimateSmetService) {
        this.estimateSmetService = estimateSmetService;
    }

    @Autowired
    public void setWBSJSONTree(WBSJSONTree wbsJsonTree) {
        this.wbsJsonTree = wbsJsonTree;
    }

    @Autowired
    public void setSmetJsonTree(SmetJsonTree smetJsonTree) {
        this.smetJsonTree = smetJsonTree;
    }

    @GetMapping(value = "/get_project", produces = "application/json")
    public ResponseEntity<?> editWBS(@RequestParam("id") String id) throws JsonProcessingException {
        List<WBS> wbsList = wbsService.findByProjectId(Integer.valueOf(id));
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(wbsList);

        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/get_wbs_list", produces = "application/json")
    public ResponseEntity<?> getWbsList(@RequestParam("id") String id) throws JsonProcessingException {
//        List<WBS> wbsList = wbsService.findByProjectId(Integer.valueOf(id));
//
//        WBSJSONTree WBSJSONTree = new WBSJSONTree(wbsList);
//        List<WBSJson> wbsJsonList = WBSJSONTree.getWbsJsonList();
        List<WBSJson> wbsJsonList = wbsJsonTree.convertProjectWBSList(Integer.valueOf(id));

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JodaModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.addMixIn(Object.class, WBSInnerObjectsMixin.class);

        String result = mapper.writeValueAsString(wbsJsonList);

        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/get_smet", produces = "application/json")
    public ResponseEntity<?> getSmet(@RequestParam("id") String id) throws JsonProcessingException {
        //TODO replace hardcoded values with tests
        List<SmetTypeJson> smetTypeJsonList = smetJsonTree.convertSmetToJson(1);

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JodaModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String result = mapper.writeValueAsString(smetTypeJsonList);

        return ResponseEntity.ok(result);
    }
}
