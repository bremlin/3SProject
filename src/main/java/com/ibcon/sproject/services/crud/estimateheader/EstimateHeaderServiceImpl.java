package com.ibcon.sproject.services.crud.estimateheader;

import com.ibcon.sproject.domain.smet.EstimateHeader;
import com.ibcon.sproject.repositories.EstimateHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstimateHeaderServiceImpl implements EstimateHeaderService {

    private EstimateHeaderRepository estimateHeaderRepository;

    @Autowired
    public void setEstimateHeaderRepository(EstimateHeaderRepository estimateHeaderRepository) {
        this.estimateHeaderRepository = estimateHeaderRepository;
    }

    @Override
    public List<?> listAll() {
        List<EstimateHeader> estimateHeaderList = new ArrayList<>();
        estimateHeaderRepository.findAll().forEach(estimateHeaderList::add);
        return estimateHeaderList;
    }

    @Override
    public EstimateHeader getById(Integer id) {
        return estimateHeaderRepository.findOne(id);
    }

    @Override
    public EstimateHeader saveOrUpdate(EstimateHeader domainObject) {
        return estimateHeaderRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        estimateHeaderRepository.delete(id);
    }
}
