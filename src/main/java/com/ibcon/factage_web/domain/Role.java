package com.ibcon.factage_web.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends AbstractDomainClass {
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    private Set<Project> projects = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public void addUser(User user){
        if(!this.getUsers().contains(user)){
            this.getUsers().add(user);
        }

        if(!user.getRoles().contains(this)){
            user.getRoles().add(this);
        }
    }

    public void removeUser(User user){
        this.getUsers().remove(user);
        user.getRoles().remove(this);
    }

    public void addProject(Project project){
        if(!this.getProjects().contains(project)){
            this.getProjects().add(project);
        }

        if(!project.getRoles().contains(this)){
            project.getRoles().add(this);
        }
    }

    public void removeProject(Project project){
        this.getProjects().remove(project);
        project.getRoles().remove(this);
    }

    @Override
    public String toString() {
        return name;
    }
}
