package com.ibcon.sproject.smet.model.objects;

import com.ibcon.sproject.smet.model.objects.smr.SmrWork;
import com.ibcon.sproject.smet.model.utilits.CheckValue;
import com.ibcon.sproject.smet.model.xml.XMLVidRabGroup;
import com.ibcon.sproject.smet.model.xml.XMLVidRabGroups;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.HashMap;

public class SmrNode extends SmetTreeNode {

    private int num;
    private String code;
    private String units;

    private double quantity;

    private double ozCost;
    private double emCost;
    private double zmCost;
    private double mtCost;
    private double pzCost;
    private double tzrCost;
    private double tzmCost;

    private double ozIndex;
    private double emIndex;
    private double zmIndex;
    private double mtIndex;
    private double nrIndex;
    private double spIndex;

    private double nrCost;
    private double spCost;
    private String nrMask = "";
    private String spMask = "";

    private int chapterId;

    private int headerId;

    private int vidRabGroupId;
    private int vidRabId;

    private ArrayList<SmrWork> works = new ArrayList<>();

    //todo smrAttribute
//    private SmrAttributesCollection attributes = new SmrAttributesCollection();

    boolean isLast = false;
    private boolean disabled;

    private ArrayList<SmetResourceAssignment> smetRa = new ArrayList<>();

    private SmetNode smet;

    private String osColumn;

    private boolean isEqui;
    private boolean isSMR;
    private boolean localNorm;


    public SmrNode(Element child, XMLVidRabGroups vrg) {

        if (CheckValue.isInteger(child.getAttributeValue("Number"))) this.num = Integer.valueOf(child.getAttributeValue("Number"));
        else {
            this.num = Integer.valueOf(child.getAttributeValue("Number").substring(0, child.getAttributeValue("Number").indexOf(".")));
        }
        this.name = (child.getAttributeValue("Caption") != null ) ? child.getAttributeValue("Caption") : "";
        this.code = (child.getAttributeValue("Code") != null ) ? child.getAttributeValue("Code") : "";
        this.units = (child.getAttributeValue("Units") != null ) ? child.getAttributeValue("Units") : "";
        this.disabled = child.getAttribute("Options") != null && child.getAttributeValue("Options").contains("Inactive");

        this.vidRabId = Integer.valueOf(child.getAttribute("Vr2001") != null ? child.getAttributeValue("Vr2001") : "0");

        for (XMLVidRabGroup g : vrg.values()) {
            if (!g.containsKey(vidRabId)) continue;
            this.vidRabGroupId = g.getId();
            if (g.get(vidRabId).getOsColumn() != null) {

                this.osColumn = g.get(vidRabId).getOsColumn();
                this.isEqui = osColumn.equals("О");
                this.isSMR = osColumn.equals("М");

            } else {

                this.osColumn = "М";
                this.isEqui = false;
                this.isSMR = true;

            }
            break;
        }

        this.isSmr = true;
    }

    public void updateValues (
            HashMap<String, Double> smrValues,
            HashMap<String, Double> smrCosts,
            HashMap<String, String> smrMasks,
            ArrayList<SmetResourceAssignment> smetRaList) {

        this.quantity = smrValues.get("quantity");

        this.tzrCost = smrCosts.containsKey("TZR") ? smrCosts.get("TZR"): 0.0;
        this.ozCost = smrCosts.containsKey("OZ") ? smrCosts.get("OZ"): 0.0;
        this.tzmCost = smrCosts.containsKey("TZM") ? smrCosts.get("TZM"): 0.0;
        this.emCost = smrCosts.containsKey("EM") ? smrCosts.get("EM"): 0.0;
        this.zmCost = smrCosts.containsKey("ZM") ? smrCosts.get("ZM"): 0.0;
        this.mtCost = smrCosts.containsKey("MT") ? smrCosts.get("MT"): 0.0;
        this.pzCost = smrCosts.containsKey("PZ") ? smrCosts.get("PZ"): 0.0;

        this.nrCost = smrCosts.containsKey("NR") ? smrCosts.get("NR"): 0.0;
        this.spCost = smrCosts.containsKey("SP") ? smrCosts.get("SP"): 0.0;

        this.tzr = smrValues.containsKey("TZR") ? smrValues.get("TZR"): 0.0;
        this.oz = smrValues.containsKey("OZ") ? smrValues.get("OZ"): 0.0;
        this.tzm = smrValues.containsKey("TZM") ? smrValues.get("TZM"): 0.0;
        this.em = smrValues.containsKey("EM") ? smrValues.get("EM"): 0.0;
        this.zm = smrValues.containsKey("ZM") ? smrValues.get("ZM"): 0.0;
        this.mt = smrValues.containsKey("MT") ? smrValues.get("MT"): 0.0;
        this.pz = smrValues.containsKey("PZ") ? smrValues.get("PZ"): 0.0;

        this.nrMask = smrMasks.get("NR");
        this.spMask = smrMasks.get("SP");
        this.nr = smrValues.containsKey("NR") ? smrValues.get("NR"): 0.0;
        this.sp = smrValues.containsKey("SP") ? smrValues.get("SP"): 0.0;
        this.sum = smrValues.containsKey("SUM") && smrValues.get("SUM") != null ? smrValues.get("SUM"): 0.0;

        this.smetRa = smetRaList;

        //create();

    }

    public int getNum() {
        return num;
    }

    public String getCode() {
        return code;
    }

    public String getUnits() {
        return units;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getOzCost() {
        return ozCost;
    }

    public double getEmCost() {
        return emCost;
    }

    public double getZmCost() {
        return zmCost;
    }

    public double getMtCost() {
        return mtCost;
    }

    public double getPzCost() {
        return pzCost;
    }

    public double getTzrCost() {
        return tzrCost;
    }

    public double getTzmCost() {
        return tzmCost;
    }

    public double getOzIndex() {
        return ozIndex;
    }

    public double getEmIndex() {
        return emIndex;
    }

    public double getZmIndex() {
        return zmIndex;
    }

    public double getMtIndex() {
        return mtIndex;
    }

    public double getNrIndex() {
        return nrIndex;
    }

    public double getSpIndex() {
        return spIndex;
    }

    public double getNrCost() {
        return nrCost;
    }

    public double getSpCost() {
        return spCost;
    }

    public String getNrMask() {
        return nrMask;
    }

    public String getSpMask() {
        return spMask;
    }

    public int getChapterId() {
        return chapterId;
    }

    public int getHeaderId() {
        return headerId;
    }

    public int getVidRabGroupId() {
        return vidRabGroupId;
    }

    public int getVidRabId() {
        return vidRabId;
    }

    public ArrayList<SmrWork> getWorks() {
        return works;
    }

    public boolean isLast() {
        return isLast;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public ArrayList<SmetResourceAssignment> getSmetRa() {
        return smetRa;
    }

    public SmetNode getSmet() {
        return smet;
    }

    public String getOsColumn() {
        return osColumn;
    }

    public boolean isEqui() {
        return isEqui;
    }

    public boolean isSMR() {
        return isSMR;
    }

    public boolean isLocalNorm() {
        return localNorm;
    }

    public void setLocalNorm(boolean localNorm) {
        this.localNorm = localNorm;
    }
}
