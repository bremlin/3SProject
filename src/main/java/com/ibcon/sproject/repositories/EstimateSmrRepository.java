package com.ibcon.sproject.repositories;

import com.ibcon.sproject.domain.smet.EstimateSmr;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EstimateSmrRepository extends CrudRepository<EstimateSmr, Integer> {
    List<EstimateSmr> findAllBySmetId(Integer smetId);
    List<EstimateSmr> findAllByChapterIdAndHeaderId(Integer chapterId, Integer headerId);
    List<EstimateSmr> findAllByChapterId(Integer chapterId);
    List<EstimateSmr> findAllByHeaderId(Integer headerId);
}
