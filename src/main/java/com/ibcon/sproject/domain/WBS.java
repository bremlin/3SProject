package com.ibcon.sproject.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Proxy;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EntityScan
@Entity
//@Proxy(lazy = false)    //без этого не получается достать projectId из wbs перед сохранением
@Table(name = "wbs")
@EqualsAndHashCode(exclude = "project")
public @Data class WBS extends AbstractDomainClass {
//    private Integer projectId;
    private Integer projectObjectId;
    private String name;
    private String code;
    private Integer parentId;
    private Integer indexNumber;

    //TODO ПОСМОТРЕТЬ ПРИМЕР
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "wbs")
    private Set<Activity> activities = new HashSet<>();

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    @JoinTable(name = "projects", joinColumns = @JoinColumn(name = "id"),
//            inverseJoinColumns = @JoinColumn(name = "project_id"))
    @JoinColumn(name = "project_id")
    private Project project = new Project();
}
