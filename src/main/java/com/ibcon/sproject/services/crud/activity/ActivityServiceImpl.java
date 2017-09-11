package com.ibcon.sproject.services.crud.activity;

import com.ibcon.sproject.domain.Activity;
import com.ibcon.sproject.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    private ActivityRepository activityRepository;

    @Autowired
    public void setActivityRepository(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public List<?> listAll() {
        List<Activity> activityList = new ArrayList<>();
        activityRepository.findAll().forEach(activityList::add);
        return activityList;
    }

    @Override
    public Activity getById(Integer id) {
        return activityRepository.findOne(id);
    }

    @Override
    public Activity saveOrUpdate(Activity domainObject) {
        if (domainObject.getProjectId() == null) {
            Integer projectId = domainObject.getWbs().getProject().getId();
            domainObject.setProjectId(projectId);
        }
        return activityRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        activityRepository.delete(id);
    }
}
