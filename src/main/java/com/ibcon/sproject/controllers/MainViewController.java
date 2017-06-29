package com.ibcon.sproject.controllers;

import com.ibcon.sproject.services.crud.wbs.WBSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainViewController {
    private WBSService wbsService;

    @Autowired
    public void setWbsService(WBSService wbsService) {
        this.wbsService = wbsService;
    }

    @RequestMapping(value = "/project_view/{id}", method = RequestMethod.GET)
    public String viewProject(@PathVariable Integer id, Model model) {
        //TODO проверить списки с activities для каждой wbs
        model.addAttribute("wbsList", wbsService.findByProjectId(id));
        return "project_view";
    }
}
