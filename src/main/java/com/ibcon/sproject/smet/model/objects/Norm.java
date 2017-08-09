package com.ibcon.sproject.smet.model.objects;

import com.ibcon.sproject.smet.model.objects.smr.SmrMask;
import org.jdom2.Element;

public class Norm {

    private String caption;
    private Double nr, sp;
    private String nrMask, spMask;
    private String normId;

    public Norm(String caption) {
        this.caption = caption;
    }

    //Расчет НР по общему нормативу
    public Norm(String caption, Double nr, String nrMask) {
        this.caption = caption;
        this.nr = nr;
        this.nrMask = nrMask;
    }

    public Norm(Element e) {

        if (e.getAttribute("PlanMask") != null && e.getAttribute("Plan") != null) {
            sp = (e.getAttributeValue("Plan").length() > 0 ? Double.parseDouble(e.getAttributeValue("Plan").replace(",", ".")) / 100 : 0);
            spMask = SmrMask.getMask(e.getAttribute("PlanMask"));
        }

        if (e.getAttribute("NaclMask") != null && e.getAttribute("Nacl") != null) {
            nr = (e.getAttributeValue("Nacl").length() > 0 ? Double.parseDouble(e.getAttributeValue("Nacl").replace(",", ".")) / 100 : 0);
            nrMask = SmrMask.getMask(e.getAttribute("NaclMask"));
        }

    }

    public String getCaption() {
        return caption;
    }

    public Double getNr() {
        return nr;
    }

    public Double getSp() {
        return sp;
    }

    public String getNrMask() {
        return nrMask;
    }

    public String getSpMask() {
        return spMask;
    }

    public String getNormId() {
        return normId;
    }

    public void setSp(Double sp) {
        this.sp = sp;
    }

    public void setSpMask(String spMask) {
        this.spMask = spMask;
    }
}
