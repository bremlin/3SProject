package com.ibcon.sproject.converters.wbstojson;

import com.ibcon.sproject.domain.Activity;
import com.ibcon.sproject.domain.WBS;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public @Data class W2uiChildren {
    private List<WBSJson> children = new ArrayList<>();

    public W2uiChildren() {
    }

    public W2uiChildren(Set<WBS> wbsSet) {
        wbsSet.forEach(wbs -> children.add(new WBSJson(wbs)));
    }

    public void addChildren(Set<Activity> activities) {
        activities.forEach(activity -> children.add(new WBSJson(activity)));
    }

    public void setChildren(Set<Activity> activities) {
        activities.forEach(activity -> children.add(new WBSJson(activity)));
    }
}
