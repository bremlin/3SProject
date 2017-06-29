package com.ibcon.sproject.listeners;

import com.ibcon.sproject.domain.Activity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class ActivityListener {
    @PreUpdate
    @PrePersist
    public void setProjectId(Activity activity) {
        activity.setProjectId(activity.getWbs().getProject().getId());
    }
}
