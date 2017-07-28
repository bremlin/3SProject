package com.ibcon.sproject.services.crud.estimatesmet;

import com.ibcon.sproject.domain.smet.EstimateSmet;
import com.ibcon.sproject.repositories.EstimateSmetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstimateSmetServiceImpl implements EstimateSmetService {

    private EstimateSmetRepository estimateSmetRepository;

    @Autowired
    public void setEstimateSmetRepository(EstimateSmetRepository estimateSmetRepository) {
        this.estimateSmetRepository = estimateSmetRepository;
    }

    @Override
    public List<?> listAll() {
        List<EstimateSmet> estimateSmetList = new ArrayList<>();
        estimateSmetRepository.findAll().forEach(estimateSmetList::add);
        return estimateSmetList;
    }

    @Override
    public EstimateSmet getById(Integer id) {
        return estimateSmetRepository.findOne(id);
    }

    @Override
    public EstimateSmet saveOrUpdate(EstimateSmet domainObject) {
        return estimateSmetRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        estimateSmetRepository.delete(id);
    }
}
