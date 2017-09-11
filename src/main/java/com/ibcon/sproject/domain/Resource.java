package com.ibcon.sproject.domain;

import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EntityScan
@Entity
@Table(name = "rsrc")
public @Data class Resource extends AbstractDomainClass{
    @Column(name = "rsrc_id")
    private Integer resourceId;

    @Column(name = "rsrc_short_name")
    private String resourceShortName;

    @Column(name = "rsrc_name")
    private String resourceName;

    @Column(name = "rsrc_type")
    private String resourceType;

    private Integer parentId;

    private String uom;

    private Integer innerType;

    @Column(name = "dbname")
    private String dnName;
}
