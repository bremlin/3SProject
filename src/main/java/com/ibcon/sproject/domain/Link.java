package com.ibcon.sproject.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ibcon.sproject.domain.smet.EstimateSmr;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.math.BigDecimal;

@EntityScan
@Entity
@Table(name = "link")
@EqualsAndHashCode(exclude = {"project", "activity", "smr"})
public @Data class Link extends AbstractDomainClass {

    public Link() {}

    public Link(Activity activity, EstimateSmr smr) {
        this.actual = true;
        this.activity = activity;
        this.smr = smr;
        this.projectId = activity.getProjectId();
        this.factor = smr.getFactor();
        this.fullFactor = smr.getFullFactor();
        this.pvApply = false;
        this.volume = smr.getQuantity().divide(fullFactor, 6).multiply(factor);
        this.tzrVolume = smr.getTzr().divide(fullFactor, 6).multiply(factor);
        this.mechVolume = smr.getTzm().divide(fullFactor, 6).multiply(factor);
        this.costEm = smr.getEmCost() == null ? BigDecimal.ZERO : smr.getEmCost();
        this.costEmm = smr.getCostEmm() == null ? BigDecimal.ZERO : smr.getCostEmm();
        this.costFot = smr.getCostFot() == null ? BigDecimal.ZERO : smr.getCostFot();
        this.costKon = smr.getCostKon() == null ? BigDecimal.ZERO : smr.getCostKon();
        this.costMat = smr.getMtCost() == null ? BigDecimal.ZERO : smr.getMtCost();
        this.costMtr = smr.getCostMtr() == null ? BigDecimal.ZERO : smr.getCostMtr();
        this.costSum = smr.getSum() == null ? BigDecimal.ZERO : smr.getSum();
        this.costOz = smr.getOzCost() == null ? BigDecimal.ZERO : smr.getOzCost();
        this.costZm = smr.getZmCost() == null ? BigDecimal.ZERO : smr.getZmCost();
        this.costNr = smr.getNrCost() == null ? BigDecimal.ZERO : smr.getNrCost();
        this.costSp = smr.getSpCost() == null ? BigDecimal.ZERO : smr.getSpCost();
        this.rsrcInnerType = 0;
    }

    private Boolean actual;

    private Integer projectId;

    @Column(name = "factor", columnDefinition = "decimal")
    private BigDecimal factor;

    @Column(name = "fullFactor", columnDefinition = "decimal")
    private BigDecimal fullFactor;

    private Boolean pvApply;

    @Column(name = "volume", columnDefinition = "decimal")
    private BigDecimal volume;

    @Column(name = "tzrVolume", columnDefinition = "decimal")
    private BigDecimal tzrVolume;

    @Column(name = "mechVolume", columnDefinition = "decimal")
    private BigDecimal mechVolume;

    @Column(name = "costOz", columnDefinition = "decimal")
    private BigDecimal costOz;

    @Column(name = "costEm", columnDefinition = "decimal")
    private BigDecimal costEm;

    @Column(name = "costZm", columnDefinition = "decimal")
    private BigDecimal costZm;

    @Column(name = "costMat", columnDefinition = "decimal")
    private BigDecimal costMat;

    @Column(name = "costNr", columnDefinition = "decimal")
    private BigDecimal costNr;

    @Column(name = "costSp", columnDefinition = "decimal")
    private BigDecimal costSp;

    @Column(name = "costEmm", columnDefinition = "decimal")
    private BigDecimal costEmm;

    @Column(name = "costFot", columnDefinition = "decimal")
    private BigDecimal costFot;

    @Column(name = "costMtr", columnDefinition = "decimal")
    private BigDecimal costMtr;

    @Column(name = "costKon", columnDefinition = "decimal")
    private BigDecimal costKon;

    @Column(name = "costSum", columnDefinition = "decimal")
    private BigDecimal costSum;

    private Integer rsrcInnerType;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
//    @JsonBackReference
    private Activity activity;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "smet_row_id")
//    @JsonBackReference
    private EstimateSmr smr;
}
