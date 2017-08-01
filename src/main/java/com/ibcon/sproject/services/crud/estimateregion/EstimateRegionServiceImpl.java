package com.ibcon.sproject.services.crud.estimateregion;

import com.ibcon.sproject.domain.smet.EstimateRegion;
import com.ibcon.sproject.repositories.EstimateRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstimateRegionServiceImpl implements EstimateRegionService {

    private EstimateRegionRepository estimateRegionRepository;

    @Autowired
    public void setEstimateRegionRepository(EstimateRegionRepository estimateRegionRepository) {
        this.estimateRegionRepository = estimateRegionRepository;
    }

    @Override
    public List<?> listAll() {
        List<EstimateRegion> estimateRegionList = new ArrayList<>();
        estimateRegionRepository.findAll().forEach(estimateRegionList::add);
        return estimateRegionList;
    }

    @Override
    public EstimateRegion getById(Integer id) {
        return estimateRegionRepository.findOne(id);
    }

    @Override
    public EstimateRegion saveOrUpdate(EstimateRegion domainObject) {
        return estimateRegionRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        estimateRegionRepository.delete(id);
    }
}
