package com.ibcon.sproject.smet.model.xml;

import com.ibcon.sproject.smet.model.objects.smr.SmrMask;
import org.jdom2.Element;

public class XMLVidRab {

    private String caption, naclMask, planMask;
    private String osColumn;

    private int id;
    private int vrGroupId;

    private Double nacl, plan;
    private Double naclK, planK;

    public XMLVidRab(Element XMLVidRab, int vrGroupId, Element root) {

        boolean nkApply = root.getChild("Parameters").getChild("CommonNK") != null
                && root.getChild("Parameters").getChild("CommonNK").getAttributeValue("ActiveItems") != null
                && root.getChild("Parameters").getChild("CommonNK").getAttributeValue("ActiveItems").contains("Vk");

        boolean spApply = root.getChild("Parameters").getChild("CommonPK") != null
                && root.getChild("Parameters").getChild("CommonPK").getAttributeValue("ActiveItems") != null
                && root.getChild("Parameters").getChild("CommonPK").getAttributeValue("ActiveItems").contains("Vk");

        this.id = Integer.parseInt(XMLVidRab.getAttributeValue("ID"));
        this.caption = XMLVidRab.getAttributeValue("Caption");
        this.vrGroupId = vrGroupId;

        if (XMLVidRab.getAttribute("Nacl") != null) {

            this.nacl = Double.parseDouble(XMLVidRab.getAttributeValue("Nacl").replace(",", ".")) / 100;
            this.naclMask = SmrMask.getMask(XMLVidRab.getAttribute("NaclMask"));
            if (XMLVidRab.getAttribute("NKB") != null && nkApply) this.naclK = Double.parseDouble(XMLVidRab.getAttributeValue("NKB").replace(",", "."));
        }

        if (XMLVidRab.getAttribute("Plan") != null) {

            this.plan = Double.parseDouble(XMLVidRab.getAttributeValue("Plan").replace(",", ".")) / 100;
            this.planMask = SmrMask.getMask(XMLVidRab.getAttribute("PlanMask"));
            if (XMLVidRab.getAttribute("PNB") != null && spApply) this.planK = Double.parseDouble(XMLVidRab.getAttributeValue("PNB").replace(",", "."));
        }

        if (XMLVidRab.getAttribute("OsColumn") != null) this.osColumn = XMLVidRab.getAttributeValue("OsColumn");

    }

    public String getCaption() {
        return caption;
    }

    public String getNaclMask() {
        return naclMask;
    }

    public String getPlanMask() {
        return planMask;
    }

    public String getOsColumn() {
        return osColumn;
    }

    public int getId() {
        return id;
    }

    public int getVrGroupId() {
        return vrGroupId;
    }

    public Double getNacl() {
        return nacl;
    }

    public Double getPlan() {
        return plan;
    }

    public Double getNaclK() {
        return naclK;
    }

    public Double getPlanK() {
        return planK;
    }
}
