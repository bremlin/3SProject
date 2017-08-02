package com.ibcon.sproject.domain.smet;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ibcon.sproject.domain.AbstractDomainClass;
import com.ibcon.sproject.domain.User;
import com.ibcon.sproject.domain.WBS;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@EntityScan
@Entity
@Table(name = "estimate_Smet")
@EqualsAndHashCode(exclude = {"userCreated", "userChanged", "region", "wbsSet", "smrSet"})
public @Data
class EstimateSmet extends AbstractDomainClass {
    private String name;

    private String num;

    @Column(columnDefinition = "tinyint")
    private Integer isVirtual;

    private String comment;

    private String status;

    private DateTime dateCreate;

    private DateTime dateChange;

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

    @OneToMany(mappedBy = "smet")
    private Set<EstimateChapter> chapters;

    @OneToMany(mappedBy = "smet")
    private Set<EstimateHeader> headers;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_create")
    private User userCreated;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_change")
    private User userChanged;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private EstimateRegion region;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "estimate_smet_wbs", joinColumns = @JoinColumn(name = "smet_id"),
            inverseJoinColumns = @JoinColumn(name = "wbs_id"))
    @JsonBackReference
    private Set<WBS> wbsSet = new HashSet<>();

    @OneToMany(mappedBy = "smet")
    private Set<EstimateSmr> smrSet = new HashSet<>();
}
