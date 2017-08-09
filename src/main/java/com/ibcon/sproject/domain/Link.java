package com.ibcon.sproject.domain;

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
    private Boolean actual;

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
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "smet_row_id")
    private EstimateSmr smr;
}
