package com.ibcon.sproject.services.crud.link;

import com.ibcon.sproject.domain.Activity;
import com.ibcon.sproject.domain.Link;
import com.ibcon.sproject.domain.smet.EstimateSmr;
import com.ibcon.sproject.repositories.ActivityRepository;
import com.ibcon.sproject.repositories.EstimateSmrRepository;
import com.ibcon.sproject.repositories.LinkRepository;
import com.ibcon.sproject.services.crud.activity.ActivityService;
import com.ibcon.sproject.services.crud.estimatesmr.EstimateSmrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {

    private LinkRepository linkRepository;
    private ActivityService activityService;
    private EstimateSmrService estimateSmrService;

    @Autowired
    public void setLinkRepository(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Autowired
    public void setEstimateSmrService(EstimateSmrService estimateSmrService) {
        this.estimateSmrService = estimateSmrService;
    }

    @Override
    public List<?> listAll() {
        List<Link> linkList = new ArrayList<>();
        linkRepository.findAll().forEach(linkList::add);
        return linkList;
    }

    @Override
    public Link getById(Integer id) {
        return linkRepository.findOne(id);
    }

    @Override
    public Link saveOrUpdate(Link domainObject) {
        return linkRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        linkRepository.delete(id);
    }

    @Override
    public List<Link> findAllByActivityId(Integer activityId) {
        return linkRepository.findAllByActivity_Id(activityId);
    }

    @Override
    public Link findByActivityIdAndSmrId(Integer activityId, Integer smrId) {
        Link link = linkRepository.findByActivity_IdAndSmr_Id(activityId, smrId);
        if (link == null) {
            Activity activity = activityService.getById(activityId);
            EstimateSmr smr = estimateSmrService.getById(smrId);
            link = new Link(activity, smr);
        }

        return link;
    }
}
