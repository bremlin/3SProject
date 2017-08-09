package com.ibcon.sproject.domain.smet;

import com.ibcon.sproject.domain.AbstractDomainClass;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import javax.persistence.Table;

@EntityScan
@Entity
@Table(name = "estimate_Region")
public @Data class EstimateRegion extends AbstractDomainClass {
    private String name;
}
