package com.ibcon.sproject.services.crud.estimatechapter;

import com.ibcon.sproject.domain.smet.EstimateChapter;
import com.ibcon.sproject.repositories.EstimateChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EstimateChapterServiceImpl implements EstimateChapterService {

    private EstimateChapterRepository estimateChapterRepository;

    @Autowired
    public void setEstimateChapterRepository(EstimateChapterRepository estimateChapterRepository) {
        this.estimateChapterRepository = estimateChapterRepository;
    }

    @Override
    public List<?> listAll() {
        List<EstimateChapter> estimateChapterList = new ArrayList<>();
        estimateChapterRepository.findAll().forEach(estimateChapterList::add);
        return estimateChapterList;
    }

    @Override
    public EstimateChapter getById(Integer id) {
        return estimateChapterRepository.findOne(id);
    }

    @Override
    public EstimateChapter saveOrUpdate(EstimateChapter domainObject) {
        return estimateChapterRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        estimateChapterRepository.delete(id);
    }

    @Override
    public List<EstimateChapter> findAllBySmetId(Integer smetId) {
        return estimateChapterRepository.findAllBySmetId(smetId);
    }
}
