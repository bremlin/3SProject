package com.ibcon.sproject.smet.model.xml;

import org.jdom2.Element;

import java.util.HashMap;
import java.util.Map;

public class XMLIndexes {

    private Map<String, XMLIndex> indexesCode;
    private String indexMode;

    public XMLIndexes(Element index) {
        this.indexesCode = new HashMap<>();

        if (index.getAttribute("IndexesMode") != null && !index.getAttributeValue("IndexesMode").equals("")) {
            this.indexMode = index.getAttributeValue("IndexesMode");
        }

        for (Element in : index.getChildren()) {
            if (!in.getName().equals("IndexesPos")) continue;
            for (Element i : in.getChildren()) {
                this.indexesCode.put(i.getAttribute("Code") != null ? i.getAttributeValue("Code") : "", new XMLIndex(i));
            }
        }
    }

    public String getIndexMode() {
        return (indexMode != null) ? indexMode : "";
    }

    public Map<String, XMLIndex> getIndexesCode() {
        return indexesCode;
    }
}
