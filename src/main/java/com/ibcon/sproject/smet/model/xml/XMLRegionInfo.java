package com.ibcon.sproject.smet.model.xml;

import org.jdom2.Element;

public class XMLRegionInfo {

    private String regionName;
    private Integer regionId;

    public XMLRegionInfo(Element region) {

        if (region.getAttribute("RegionName") != null) {
            regionName = region.getAttributeValue("RegionName");
        }
        if (region.getAttribute("RegionID") != null) {
            regionId = Integer.valueOf(region.getAttributeValue("RegionID"));
        }
    }

    public String getRegionName() {
        return regionName;
    }

    public Integer getRegionId() {
        return regionId;
    }
}
