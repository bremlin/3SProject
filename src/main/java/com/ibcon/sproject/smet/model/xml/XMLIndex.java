package com.ibcon.sproject.smet.model.xml;

import org.jdom2.Element;

import java.util.HashMap;
import java.util.Map;

public class XMLIndex {

    private String code;
    private Map<String, Double> values;

    public XMLIndex(Element index) {
        this.code = index.getAttribute("Code") != null ? index.getAttributeValue("Code") : "";
        this.values = new HashMap<>();

        if (index.getAttribute("EM") != null && !index.getAttributeValue("EM").equals("")) {
            this.values.put("EM", Double.parseDouble(index.getAttributeValue("EM").replace(",", ".")));
        }

        if (index.getAttribute("OZ") != null && !index.getAttributeValue("OZ").equals("")) {
            this.values.put("OZ", Double.parseDouble(index.getAttributeValue("OZ").replace(",", ".")));
        }

        if (index.getAttribute("MT") != null && !index.getAttributeValue("MT").equals("")) {
            this.values.put("MT", Double.parseDouble(index.getAttributeValue("MT").replace(",", ".")));
        }


        if (index.getAttribute("ZM") != null && !index.getAttributeValue("ZM").equals("")) {
            this.values.put("ZM", Double.parseDouble(index.getAttributeValue("ZM").replace(",", ".")));
        }
    }

    public String getCode() {
        return code;
    }

    public Map<String, Double> getValues() {
        return values;
    }
}
