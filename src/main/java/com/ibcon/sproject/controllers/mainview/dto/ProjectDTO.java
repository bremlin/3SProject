package com.ibcon.sproject.controllers.mainview.dto;

import com.ibcon.sproject.domain.*;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public @Data class ProjectDTO {
    private Integer id;
    private Integer projectObjectId;
    private String primaveraId;
    private String projectName;
    private String projectDbName;
    private Integer projectWbsId;
    private String projectCode;
    private Integer configId;
    private Integer indexNumber;
    private Set<Role> roles;
    private Set<com.ibcon.sproject.domain.WBS> wbsSet;

    private List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    public ProjectDTO(Project project) {
        this.id = project.getId();
        this.projectObjectId = project.getProjectObjectId();
        this.primaveraId = project.getPrimaveraId();
        this.projectName = project.getProjectName();
        this.projectDbName = project.getProjectDbName();
        this.projectWbsId = project.getProjectWbsId();
        this.projectCode = project.getProjectCode();
        this.configId = project.getConfigId();
        this.indexNumber = project.getIndexNumber();
        this.roles = project.getRoles();
        this.wbsSet = project.getWbsSet();

        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
    }
}
