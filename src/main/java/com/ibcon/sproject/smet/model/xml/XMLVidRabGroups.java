package com.ibcon.sproject.smet.model.xml;

import org.jdom2.Element;

import java.util.HashMap;

public class XMLVidRabGroups extends HashMap<Integer, XMLVidRabGroup> {

    private int id;
    private String caption;

    public XMLVidRabGroups(Element r) {

        Element e = r.getChild("VidRab_Catalog");

        for (Element eChild : e.getChildren("Vids_Rab")) {
            if (!eChild.getAttributeValue("Type").contains("2001") || eChild.getChildren().size() < 1) continue;

            for (Element eVrg : eChild.getChildren()) {
                if (!eVrg.getName().equals("VidRab_Group")) continue;
                put(Integer.parseInt(eVrg.getAttributeValue("ID")), new XMLVidRabGroup(eVrg, r));
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }
}
