package com.ibcon.sproject.smet.model.xml;

import com.ibcon.sproject.smet.model.objects.SmetTreeNode;

public class HeaderNode extends SmetTreeNode {

    private int chapterId;

    public HeaderNode(String caption) {
        this.name = (name != null) ? name : "";
        this.isHeader = true;
    }


    public int getChapterId() {
        return chapterId;
    }
}
