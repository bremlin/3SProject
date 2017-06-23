package com.ibcon.sproject.creators.project;

import com.ibcon.sproject.domain.Role;

import java.util.List;

public class EmptyProjectFormCreatorImpl extends AbstractProjectFormModelCreator {
    public EmptyProjectFormCreatorImpl() {
    }

    public EmptyProjectFormCreatorImpl(List<?> objects) {
        this.allRoles = (List<Role>) objects;
    }
}
