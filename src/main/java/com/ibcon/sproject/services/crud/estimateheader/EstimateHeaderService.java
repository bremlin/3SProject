package com.ibcon.sproject.services.crud.estimateheader;

import com.ibcon.sproject.domain.smet.EstimateHeader;
import com.ibcon.sproject.services.crud.CrudService;

import java.util.List;

public interface EstimateHeaderService extends CrudService<EstimateHeader> {
    public List<EstimateHeader> findAllBySmetId(Integer smetId);
}
