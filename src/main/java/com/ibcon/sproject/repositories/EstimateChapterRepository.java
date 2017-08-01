package com.ibcon.sproject.repositories;

import com.ibcon.sproject.domain.smet.EstimateChapter;
import com.ibcon.sproject.domain.smet.EstimateSmet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EstimateChapterRepository extends CrudRepository<EstimateChapter, Integer> {
    List<EstimateChapter> findAllBySmetId(Integer smetId);
}
