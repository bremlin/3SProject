package com.ibcon.sproject.services.crud.estimatesmr;

import com.ibcon.sproject.domain.smet.EstimateSmr;
import com.ibcon.sproject.repositories.EstimateSmrRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstimateSmrServiceImpl implements EstimateSmrService {

    private EstimateSmrRepository estimateSmrRepository;

    @Autowired
    public void setEstimateSmrRepository(EstimateSmrRepository estimateSmrRepository) {
        this.estimateSmrRepository = estimateSmrRepository;
    }

    @Override
    public List<?> listAll() {
        List<EstimateSmr> estimateSmrList = new ArrayList<>();
        estimateSmrRepository.findAll().forEach(estimateSmrList::add);
        return estimateSmrList;
    }

    @Override
    public EstimateSmr getById(Integer id) {
        return estimateSmrRepository.findOne(id);
    }

    @Override
    public EstimateSmr saveOrUpdate(EstimateSmr domainObject) {
        return estimateSmrRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        estimateSmrRepository.delete(id);
    }

    @Override
    public List<EstimateSmr> findAllBySmetId(Integer smetId) {
        return estimateSmrRepository.findAllBySmetId(smetId);
    }

    @Override
    public List<EstimateSmr> findAllByChapterIdAndHeaderId(Integer chapterId, Integer headerId) {
        return estimateSmrRepository.findAllByChapterIdAndHeaderId(chapterId, headerId);
    }

    @Override
    public List<EstimateSmr> findAllByChapterId(Integer chapterId) {
        return estimateSmrRepository.findAllByChapterId(chapterId);
    }

    @Override
    public List<EstimateSmr> findAllByHeaderId(Integer headerId) {
        return estimateSmrRepository.findAllByHeaderId(headerId);
    }
}
