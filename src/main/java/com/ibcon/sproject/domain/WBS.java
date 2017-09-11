package com.ibcon.sproject.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ibcon.sproject.domain.smet.EstimateSmet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Proxy;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

//TODO брать проект из toString()
@EntityScan
@Entity
@Table(name = "wbs")
@EqualsAndHashCode(exclude = {"project", "activities", "wbsSet", "estimateSmets"})
@ToString(exclude = {"project", "activities", "wbsSet", "estimateSmets"})
public @Data class WBS extends AbstractDomainClass {
//    private Integer projectId;
    private Integer projectObjectId;
    private String name;
    private String code;
    private Integer parentId;
    private Integer indexNumber;

    //TODO ПОСМОТРЕТЬ ПРИМЕР
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "wbs")
    @JsonManagedReference
    private Set<Activity> activities = new HashSet<>();

    @OneToMany
    @JoinColumn(name="parentId")
    private Set<WBS> wbsSet = new HashSet<>();

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
//    @JoinTable(name = "projects", joinColumns = @JoinColumn(name = "id"),
//            inverseJoinColumns = @JoinColumn(name = "project_id"))
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project = new Project();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "wbsSet")
    @JsonManagedReference
    private Set<EstimateSmet> estimateSmets = new HashSet<>();
}
