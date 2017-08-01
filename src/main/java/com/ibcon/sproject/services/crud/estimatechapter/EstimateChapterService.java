package com.ibcon.sproject.services.crud.estimatechapter;

import com.ibcon.sproject.domain.smet.EstimateChapter;
import com.ibcon.sproject.services.crud.CrudService;

import java.util.List;

public interface EstimateChapterService extends CrudService<EstimateChapter> {
    List<EstimateChapter> findAllBySmetId(Integer smetId);
}
