package com.ibcon.sproject.smet.model.objects;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

public class SmetTreeNode extends DefaultMutableTreeNode{

    private int         id;
    private int         smetId;
    protected String    name;

    protected double      oz = 0.0;
    protected double      em = 0.0;
    protected double      zm = 0.0;
    protected double      mt = 0.0;
    protected double      pz = 0.0;
    protected double      nr = 0.0;
    protected double      sp = 0.0;
    protected double      sum = 0.0;
    protected double      tzr = 0.0;
    protected double      tzm = 0.0;

    protected boolean isSmet = false;
    protected boolean isChapter = false;
    protected boolean isHeader = false;
    protected boolean isSmr = false;
    protected boolean isRoot = false;
    protected boolean isHeadSmet = false;
    protected boolean isHeadSmr = false;
    public boolean smetUnderExpanded = false;
    private boolean isDummyChapter = false;

    private int indexId;
    private int indexType;

    private String indexCode = "";
    private String indexName = "";

    private double indexSum;
    private double index;

    public SmetTreeNode() {
    }

    //Конструктор для ROOT
    public SmetTreeNode(int id, String name) {

        this.id = id;
        this.name = name;
        this.isRoot = true;
        setUserObject(id + "__" + name);

    }
    //Конструктор для шапок
    public SmetTreeNode(String userObject) {

        if (userObject.equals("smethead")) {

            setUserObject("smethead");
            this.isHeadSmet = true;

        } else if (userObject.equals("smrhead")) {

            setUserObject("smrhead");
            this.isHeadSmr = true;

        } else if (userObject.equals("dummyChapter")) {

            setUserObject("dummyChapter");
            this.isDummyChapter = true;

        }

    }

    public List<SmetTreeNode> getChildren() {return children; }

    public int getId() {
        return id;
    }

    public int getSmetId() {
        return smetId;
    }

    public String getName() {
        return name;
    }

    public Double getOz() {
        return oz;
    }

    public Double getEm() {
        return em;
    }

    public Double getZm() {
        return zm;
    }

    public Double getMt() {
        return mt;
    }

    public Double getPz() {
        return pz;
    }

    public Double getNr() {
        return nr;
    }

    public Double getSp() {
        return sp;
    }

    public Double getSum() {
        return sum;
    }

    public Double getTzr() {
        return tzr;
    }

    public Double getTzm() {
        return tzm;
    }

    public boolean isSmet() {
        return isSmet;
    }

    public boolean isChapter() {
        return isChapter;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public boolean isSmr() {
        return isSmr;
    }

    @Override
    public boolean isRoot() {
        return isRoot;
    }

    public boolean isHeadSmet() {
        return isHeadSmet;
    }

    public boolean isHeadSmr() {
        return isHeadSmr;
    }

    public boolean isSmetUnderExpanded() {
        return smetUnderExpanded;
    }

    public boolean isDummyChapter() {
        return isDummyChapter;
    }

    public int getIndexId() {
        return indexId;
    }

    public int getIndexType() {
        return indexType;
    }

    public String getIndexCode() {
        return indexCode;
    }

    public String getIndexName() {
        return indexName;
    }

    public double getIndexSum() {
        return indexSum;
    }

    public double getIndex() {
        return index;
    }

    public void setOz(double oz) {
        this.oz = oz;
    }

    public void setEm(double em) {
        this.em = em;
    }

    public void setZm(double zm) {
        this.zm = zm;
    }

    public void setMt(double mt) {
        this.mt = mt;
    }

    public void setPz(double pz) {
        this.pz = pz;
    }

    public void setNr(double nr) {
        this.nr = nr;
    }

    public void setSp(double sp) {
        this.sp = sp;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public void setTzr(double tzr) {
        this.tzr = tzr;
    }

    public void setTzm(double tzm) {
        this.tzm = tzm;
    }
}
