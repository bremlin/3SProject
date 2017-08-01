package com.ibcon.sproject.converters.wbstojson;

import com.ibcon.sproject.domain.WBS;
import com.ibcon.sproject.services.crud.wbs.WBSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ComponentScan
@Component
public class WBSJSONTree {
    private WBSService wbsService;

    @Autowired
    private void setWbsService(WBSService wbsService) {
        this.wbsService = wbsService;
    }

    public List<WBSJson> convertProjectWBSList(Integer projectId) {
        List<WBS> wbsList = wbsService.findByProjectId(projectId);
        List<WBSJson> wbsJsonList = new ArrayList<>();
        wbsList.stream().filter(wbs -> wbs.getParentId() == 0).forEach(wbs -> wbsJsonList.add(new WBSJson(wbs)));
        return wbsJsonList;
    }
}
