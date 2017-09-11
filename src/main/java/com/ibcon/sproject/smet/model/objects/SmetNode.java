package com.ibcon.sproject.smet.model.objects;

import com.ibcon.sproject.smet.model.xml.XMLParameters;
import com.ibcon.sproject.smet.model.xml.XMLRegionInfo;
import org.jdom2.Element;

import java.util.Date;
import java.util.HashMap;

public class SmetNode extends SmetTreeNode {

    private String userChange = "default";
    private String userCreate = "default";
    private String filePath = "";

    private String num = "";
    private String status = "Действующая";
    private String comment = "";
    private int version = 0;

    private Date dateCreate;
    private Date dateChange;

    private Boolean isVirtual = false;
    private int epsId, epsObjectId;

    private int tzDigits = 6;
    private int matDigits = 6;
    private Boolean isRoundSum = false;

    private int regionId = 0;
    private String regionName = "";

    private String base = "";

    private HashMap<Integer, SmrNode> smetRowsByNum = new HashMap<>();


    public SmetNode(String name, Element rootNode, XMLRegionInfo regionInfo, XMLParameters params, int epsId) {
        if (rootNode.getChild("DbInfo")!= null) {
            Element smetHeader = rootNode.getChild("DbInfo").getChild("Document");
            this.name = smetHeader.getAttributeValue("Caption");
            this.num = smetHeader.getAttributeValue("LocalNumber") != null ? smetHeader.getAttributeValue("LocalNumber") : smetHeader.getAttributeValue("Caption");
        } else {
            this.name = name;
        }

        if (num.equals("") && rootNode.getChild("Properties") != null && rootNode.getChild("Properties").getAttribute("LocNum")!= null) {
            this.num = rootNode.getChild("Properties").getAttributeValue("LocNum");
        }

        this.isSmet = true;
        this.epsId = epsId;

        if (regionInfo.getRegionId() != null) this.regionId = regionInfo.getRegionId();
        if (regionInfo.getRegionName() != null) this.regionName = regionInfo.getRegionName();

        this.isRoundSum = params.getRoundSum();
        this.tzDigits = params.getTzDigits();
        this.matDigits = params.getMatDigits();

        this.userCreate = System.getProperty("user.name");
    }

    public void addSmetRow(SmrNode smetRow) {
        if (smetRowsByNum != null) smetRowsByNum.put(smetRow.getNum(), smetRow);
    }

    public void insert() {
        String sql = "INSERT INTO ";
    }

    public String getUserChange() {
        return userChange;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getNum() {
        return num;
    }

    public String getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public int getVersion() {
        return version;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public Date getDateChange() {
        return dateChange;
    }

    public Boolean getVirtual() {
        return isVirtual;
    }

    public int getEpsId() {
        return epsId;
    }

    public int getEpsObjectId() {
        return epsObjectId;
    }

    public int getTzDigits() {
        return tzDigits;
    }

    public int getMatDigits() {
        return matDigits;
    }

    public Boolean getRoundSum() {
        return isRoundSum;
    }

    public int getRegionId() {
        return regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getBase() {
        return base;
    }

    public HashMap<Integer, SmrNode> getSmetRowsByNum() {
        return smetRowsByNum;
    }
}
