package com.ibcon.sproject.services.crud.link;

import com.ibcon.sproject.domain.Link;
import com.ibcon.sproject.repositories.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {

    private LinkRepository linkRepository;

    @Autowired
    public void setLinkRepository(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
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
}
