package com.ibcon.sproject.smet.model.objects;

import org.jdom2.Element;

public class ChapterNode extends SmetTreeNode {

    private int num;
    private String sysId;
    private Double progress = 0.0;

    public ChapterNode(Element e, Integer num) {
        this.name = e.getAttributeValue("Caption");
        this.sysId = e.getAttributeValue("SysID");
        this.num = num;
        this.isChapter = true;
    }


    public int getNum() {
        return num;
    }

    public String getSysId() {
        return sysId;
    }

    public Double getProgress() {
        return progress;
    }
}
