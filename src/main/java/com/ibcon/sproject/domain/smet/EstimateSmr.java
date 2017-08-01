package com.ibcon.sproject.domain.smet;

import com.ibcon.sproject.domain.AbstractDomainClass;
import com.ibcon.sproject.domain.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.math.BigDecimal;

@EntityScan
@Entity
@Table(name = "estimate_Smr")
@EqualsAndHashCode(exclude = {"chapter", "header", "smet"})
public @Data class EstimateSmr extends AbstractDomainClass {
    private String name;

    @Column(name = "num", columnDefinition = "smallint")
    private Integer num;

    private String code;

    private String units;

    @Column(name = "quantity", columnDefinition = "decimal")
    private BigDecimal quantity;

    @Column(name = "oz", columnDefinition = "decimal")
    private BigDecimal oz;

    @Column(name = "em", columnDefinition = "decimal")
    private BigDecimal em;

    @Column(name = "zm", columnDefinition = "decimal")
    private BigDecimal zm;

    @Column(name = "mt", columnDefinition = "decimal")
    private BigDecimal mt;

    @Column(name = "pz", columnDefinition = "decimal")
    private BigDecimal pz;

    @Column(name = "nr", columnDefinition = "decimal")
    private BigDecimal nr;

    @Column(name = "sp", columnDefinition = "decimal")
    private BigDecimal sp;

    @Column(name = "sum", columnDefinition = "decimal")
    private BigDecimal sum;

    @Column(name = "tzr", columnDefinition = "decimal")
    private BigDecimal tzr;

    @Column(name = "tzm", columnDefinition = "decimal")
    private BigDecimal tzm;


    @Column(name = "oz_cost", columnDefinition = "decimal")
    private BigDecimal ozCost;

    @Column(name = "em_cost", columnDefinition = "decimal")
    private BigDecimal emCost;

    @Column(name = "zm_cost", columnDefinition = "decimal")
    private BigDecimal zmCost;

    @Column(name = "mt_cost", columnDefinition = "decimal")
    private BigDecimal mtCost;

    @Column(name = "pz_cost", columnDefinition = "decimal")
    private BigDecimal pzCost;

    @Column(name = "nr_cost", columnDefinition = "decimal")
    private BigDecimal nrCost;

    @Column(name = "sp_cost", columnDefinition = "decimal")
    private BigDecimal spCost;

    @Column(name = "tzr_cost", columnDefinition = "decimal")
    private BigDecimal tzrCost;

    @Column(name = "tzm_cost", columnDefinition = "decimal")
    private BigDecimal tzmCost;


    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private EstimateChapter chapter;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "header_id")
    private EstimateHeader header;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "smet_id")
    private EstimateSmet smet;
}
