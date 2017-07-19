package com.ibcon.sproject.converters.wbstojson;

import com.ibcon.sproject.domain.WBS;
import lombok.Data;

public @Data class WBSJson {
    private Integer id;
    private Integer projectObjectId;
    private String name;
    private String code;
    private Integer parentId;
    private Integer indexNumber;
    private Boolean ttBranch;

    public WBSJson(WBS wbs) {
        this.id = wbs.getId();
        this.parentId = wbs.getParentId();
        this.name = wbs.getName();
        this.code = wbs.getCode();
        this.projectObjectId = wbs.getProjectObjectId();
        this.indexNumber = wbs.getIndexNumber();

        if (wbs.getActivities().size() > 0) {
            ttBranch = true;
        }
        if (wbs.getWbsSet().size() > 0) {
            ttBranch = true;
        }
    }
}
