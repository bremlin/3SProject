package com.ibcon.sproject.domain.smet;

import com.ibcon.sproject.domain.AbstractDomainClass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;

@EntityScan
@Entity
@Table(name = "estimate_Header")
@EqualsAndHashCode(exclude = {"smet", "chapter"})
public @Data class EstimateHeader extends AbstractDomainClass {
    private String name;
    private String num;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "smet_id")
    private EstimateSmet smet;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private EstimateSmet chapter;
}
