package com.ibcon.sproject.creators.user;

import com.ibcon.sproject.domain.Role;

import java.util.Date;
import java.util.List;

public class EmptyUserFormCreatorImpl extends AbstractUserFormCreator {
    public EmptyUserFormCreatorImpl() {
    }

    public EmptyUserFormCreatorImpl(List<?> objects) {
        this.allRoles = (List<Role>) objects;
        getUser().setExpirationDate(new Date());
    }
}
