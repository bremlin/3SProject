package com.ibcon.sproject.converters.smettojson;

import com.ibcon.sproject.domain.smet.EstimateChapter;
import com.ibcon.sproject.domain.smet.EstimateHeader;
import com.ibcon.sproject.domain.smet.EstimateSmet;
import com.ibcon.sproject.domain.smet.EstimateSmr;
import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public @Data class SmetTypeJson {
    private String id;
    private String parentId;
    private String type;
    private String name;
    private String num;
    private Integer isVirtual;
    private String comment;
    private String status;
    private DateTime dateCreated;
    private DateTime dataChanged;
    private String userCreated;
    private String userChanged;
    private String region;
    private String code;
    private String units;
    private BigDecimal quantity;
    private BigDecimal oz;
    private BigDecimal em;
    private BigDecimal zm;
    private BigDecimal mt;
    private BigDecimal pz;
    private BigDecimal nr;
    private BigDecimal sp;
    private BigDecimal sum;
    private BigDecimal tzr;
    private BigDecimal tzm;
    private BigDecimal ozCost;
    private BigDecimal emCost;
    private BigDecimal zmCost;
    private BigDecimal mtCost;
    private BigDecimal pzCost;
    private BigDecimal nrCost;
    private BigDecimal spCost;
    private BigDecimal tzrCost;
    private BigDecimal tzmCost;
    private W2uiChildren w2ui;

    public SmetTypeJson(EstimateSmr smr) {
        this.id = "r" + smr.getId();
        this.type = "smr";
        this.name = smr.getName();
        this.num = String.valueOf(smr.getNum());
        this.units = smr.getUnits();
        this.quantity = smr.getQuantity();
        this.oz = smr.getOz();
        this.em = smr.getEm();
        this.zm = smr.getZm();
        this.mt = smr.getMt();
        this.pz = smr.getPz();
        this.nr = smr.getNr();
        this.sp = smr.getSp();
        this.sum = smr.getSum();
        this.tzr = smr.getTzr();
        this.tzm = smr.getTzm();
        this.ozCost = smr.getOzCost();
        this.emCost = smr.getEmCost();
        this.zmCost = smr.getZmCost();
        this.mtCost = smr.getMtCost();
        this.pzCost = smr.getPzCost();
        this.nrCost = smr.getNrCost();
        this.spCost = smr.getSpCost();
        this.tzrCost = smr.getTzrCost();
        this.tzmCost = smr.getTzmCost();
    }

    public SmetTypeJson(EstimateChapter chapter) {
        this.id = "c" + chapter.getId();
        this.type = "chapter";
        this.name = chapter.getName();
        this.num = chapter.getNum();
    }

    public SmetTypeJson(EstimateHeader header) {
        this.id = "h" + header.getId();
        this.type = "header";
        this.name = header.getName();
        this.num = header.getNum();
    }

    public SmetTypeJson(EstimateSmet smet) {
        this.id = "s" + smet.getId();
        this.type = "smet";
        this.name = smet.getName();
        this.num = smet.getNum();
        this.isVirtual = smet.getIsVirtual();
        this.comment = smet.getComment();
        this.status = smet.getStatus();
        this.dateCreated = smet.getDateCreate();
        this.dataChanged = smet.getDateChange();
        this.userCreated = smet.getUserCreated().getName();
        if (smet.getUserChanged() != null) {
            this.userChanged = smet.getUserChanged().getName();
        }
        this.oz = smet.getOz();
        this.em = smet.getEm();
        this.zm = smet.getZm();
        this.mt = smet.getMt();
        this.pz = smet.getPz();
        this.nr = smet.getNr();
        this.sp = smet.getSp();
        this.sum = smet.getSum();
        this.tzr = smet.getTzr();
        this.tzm = smet.getTzm();
    }

    public void addChildren(SmetTypeJson smetTypeJson) {
        smetTypeJson.setParentId(this.id);
        if (this.w2ui == null) {
            this.w2ui = new W2uiChildren();
        }
        w2ui.addChildren(smetTypeJson);
    }
}
