package com.ibcon.sproject.smet.model.objects.smr;

import com.ibcon.sproject.smet.model.objects.SmrNode;

public class SmrWork {

    private String caption = "";
    private int smrId = 0;

    public SmrWork(String caption) {
        this.caption = caption;
    }

    public SmrWork(SmrWork smrWork, SmrNode smrNode) {
        this.caption = smrWork.getCaption();
        this.smrId = smrNode.getId();
    }

    public void setSmrId(int smrId) {
        this.smrId = smrId;
    }

    public String getCaption() {
        return caption;
    }

    public int getSmrId() {
        return smrId;
    }
}
