package com.ibcon.sproject.converters.wbstojson;

import com.ibcon.sproject.domain.Activity;
import com.ibcon.sproject.domain.WBS;
import lombok.Data;

import java.math.BigDecimal;

public @Data class WBSJson {
    private String id;
    private Integer projectObjectId;
    private String name;
    private String code;
    private Integer parentId;
    private Integer indexNumber;
    private BigDecimal cost;
    private W2uiChildren w2ui;

    private String type;

    public WBSJson(WBS wbs) {
        this.id = String.valueOf(wbs.getId());
        this.parentId = wbs.getParentId();
        this.name = wbs.getName();
        this.code = wbs.getCode();
        this.projectObjectId = wbs.getProjectObjectId();
        this.indexNumber = wbs.getIndexNumber();
        this.type = "wbs";

        if (wbs.getWbsSet().size() > 0) {
            w2ui = new W2uiChildren(wbs.getWbsSet());
        }

        if (wbs.getActivities().size() > 0) {
            if (w2ui != null) {
                w2ui.addChildren(wbs.getActivities());
            } else {
                w2ui = new W2uiChildren();
                w2ui.setChildren(wbs.getActivities());
            }
        }
    }

    public WBSJson(Activity activity) {
        this.id = "_" + activity.getId();
        this.name = activity.getActivityName();
        this.projectObjectId = activity.getProjectObjectId();
        this.cost = new BigDecimal(5);
        this.type = "act";
    }
}