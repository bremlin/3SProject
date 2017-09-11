package com.ibcon.sproject.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.ibcon.sproject.converters.smettojson.SmetJsonTree;
import com.ibcon.sproject.converters.smettojson.SmetTypeJson;
import com.ibcon.sproject.converters.wbstojson.WBSJSONTree;
import com.ibcon.sproject.converters.wbstojson.WBSJson;
import com.ibcon.sproject.domain.Link;
import com.ibcon.sproject.domain.Project;
import com.ibcon.sproject.domain.WBS;
import com.ibcon.sproject.domain.mixins.WBSInnerObjectsMixin;
import com.ibcon.sproject.domain.smet.EstimateSmet;
import com.ibcon.sproject.services.crud.activity.ActivityService;
import com.ibcon.sproject.services.crud.estimatesmet.EstimateSmetService;
import com.ibcon.sproject.services.crud.link.LinkService;
import com.ibcon.sproject.services.crud.project.ProjectService;
import com.ibcon.sproject.services.crud.role.RoleService;
import com.ibcon.sproject.services.crud.user.UserServiceCrud;
import com.ibcon.sproject.services.crud.wbs.WBSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class MainTableController {
    private UserServiceCrud userServiceCrud;
    private RoleService roleService;
    private ProjectService projectService;
    private WBSService wbsService;
    private ActivityService activityService;
    private EstimateSmetService estimateSmetService;
    private LinkService linkService;

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
    public void setLinkService(LinkService linkService) {
        this.linkService = linkService;
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
        List<SmetTypeJson> smetTypeJsonList = new ArrayList<>();
        smetTypeJsonList.add(smetJsonTree.convertSmetToJson(Integer.valueOf(id)));

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JodaModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String result = mapper.writeValueAsString(smetTypeJsonList);

        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/get_smet_by_project", produces = "application/json")
    public ResponseEntity<?> getSmetByProject(@RequestParam("id") String id) throws JsonProcessingException {
        Project project = projectService.getById(Integer.valueOf(id));

        if (project == null) {
            //TODO
        }

        Set<EstimateSmet> smets = new HashSet<>();
        Set<WBS> wbsSet = project.getWbsSet();
        if (wbsSet != null) {
            wbsSet.forEach(wbs -> smets.addAll(estimateSmetService.findByWbsSetId(wbs.getId())));
        }

        if (smets == null) {
            //TODO
        }

        List<SmetTypeJson> smetTypeJsonList = new ArrayList<>();
        smets.forEach(smet -> smetTypeJsonList.add(smetJsonTree.convertSmetToJson(smet.getId())));

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JodaModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String result = mapper.writeValueAsString(smetTypeJsonList);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/links/{activityId}/{smrId}", produces = "application/json")
    public ResponseEntity<?> getLink(@PathVariable("activityId") String activityId,
                                     @PathVariable("smrId") String smrId) throws JsonProcessingException {
        Link link = linkService.findByActivityIdAndSmrId(Integer.valueOf(activityId), Integer.valueOf(smrId));

        ObjectMapper mapper = new ObjectMapper();

        String result = mapper.writeValueAsString(link);

        return ResponseEntity.ok(result);
    }
}
