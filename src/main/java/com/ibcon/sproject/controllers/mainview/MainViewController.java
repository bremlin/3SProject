package com.ibcon.sproject.controllers.mainview;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.ibcon.sproject.converters.smettojson.SmetJsonTree;
import com.ibcon.sproject.converters.smettojson.SmetTypeJson;
import com.ibcon.sproject.converters.wbstojson.WBSJSONTree;
import com.ibcon.sproject.converters.wbstojson.WBSJson;
import com.ibcon.sproject.domain.Project;
import com.ibcon.sproject.domain.WBS;
import com.ibcon.sproject.domain.mixins.WBSInnerObjectsMixin;
import com.ibcon.sproject.domain.smet.EstimateSmet;
import com.ibcon.sproject.services.crud.activity.ActivityService;
import com.ibcon.sproject.services.crud.estimatesmet.EstimateSmetService;
import com.ibcon.sproject.services.crud.project.ProjectService;
import com.ibcon.sproject.services.crud.role.RoleService;
import com.ibcon.sproject.services.crud.user.UserServiceCrud;
import com.ibcon.sproject.services.crud.wbs.WBSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainViewController {

    @RequestMapping(value = "/project_view/{id}", method = RequestMethod.GET)
    public String viewProject(@PathVariable Integer id, Model model) {
        //TODO проверить списки с activities для каждой wbs
//        model.addAttribute("wbsList", wbsService.findByProjectId(id));
        return "project_view";
    }
}