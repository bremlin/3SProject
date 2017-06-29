package com.ibcon.sproject.services.crud.wbs;

import com.ibcon.sproject.domain.WBS;
import com.ibcon.sproject.repositories.WBSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WBSServiceImpl implements WBSService {
    private WBSRepository wbsRepository;

    @Autowired
    public void setWbsRepository(WBSRepository wbsRepository) {
        this.wbsRepository = wbsRepository;
    }

    @Override
    public List<?> listAll() {
        List<WBS> wbsList = new ArrayList<>();
        wbsRepository.findAll().forEach(wbsList::add);
        return wbsList;
    }

    @Override
    public WBS getById(Integer id) {
        return wbsRepository.findOne(id);
    }

    @Override
    public WBS saveOrUpdate(WBS domainObject) {
        return wbsRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        wbsRepository.delete(id);
    }

    @Override
    public List<WBS> findByProjectId(Integer id) {
        return wbsRepository.findAllByProjectId(id);
    }
}
