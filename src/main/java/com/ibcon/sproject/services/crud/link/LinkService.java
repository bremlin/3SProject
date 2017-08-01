package com.ibcon.sproject.services.crud.link;

import com.ibcon.sproject.domain.Link;
import com.ibcon.sproject.services.crud.CrudService;

import java.util.List;

public interface LinkService extends CrudService<Link> {
    public List<Link> findAllByActivityId(Integer activityId);
}
