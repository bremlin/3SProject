package com.ibcon.sproject.smet.model.xml;

import org.jdom2.Element;

import java.util.HashMap;

public class XMLVidRabGroup extends HashMap<Integer, XMLVidRab> {

    private int id;
    private String caption;

    public XMLVidRabGroup(Element eVrg, Element r) {

        if (eVrg.getChildren().size() < 1) return;

        this.id = Integer.parseInt(eVrg.getAttributeValue("ID"));
        this.caption = eVrg.getAttributeValue("Caption");

        for (Element eVr : eVrg.getChildren()) {
            put (Integer.parseInt(eVr.getAttributeValue("ID")), new XMLVidRab(eVr, id, r));
        }

    }

    public int getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }
}
