package com.ibcon.sproject.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@EntityScan
@Entity
@Table(name = "projects")
public @Data class Project extends AbstractDomainClass {
//    private Integer id;
    private Integer projectObjectId;
    private String primaveraId;
    private String projectName;
    private String projectDbName;
    private Integer projectWbsId;
    private String projectCode;
    private Integer configId;
    private Integer indexNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "projects_roles", joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonManagedReference
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "project")
    @JsonManagedReference
    private Set<WBS> wbsSet = new HashSet<>();

    public void addRole(Role role) {
        if (!this.getRoles().contains(role)) {
            this.getRoles().add(role);
        }

        if(!role.getProjects().contains(this)){
            role.getProjects().add(this);
        }
    }

    public void removeRole(Role role){
        this.getRoles().remove(role);
        role.getProjects().remove(this);
    }
}
