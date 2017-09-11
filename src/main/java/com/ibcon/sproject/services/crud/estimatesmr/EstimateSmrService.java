package com.ibcon.sproject.services.crud.estimatesmr;

import com.ibcon.sproject.domain.smet.EstimateSmr;
import com.ibcon.sproject.services.crud.CrudService;

import java.util.List;

public interface EstimateSmrService extends CrudService<EstimateSmr> {
    List<EstimateSmr> findAllBySmetId(Integer smetId);
    List<EstimateSmr> findAllByChapterIdAndHeaderId(Integer chapterId, Integer headerId);
    List<EstimateSmr> findAllByChapterId(Integer chapterId);
    List<EstimateSmr> findAllByHeaderId(Integer headerId);
}
