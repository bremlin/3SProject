package com.ibcon.sproject.services.crud.estimateheader;

import com.ibcon.sproject.domain.smet.EstimateHeader;
import com.ibcon.sproject.domain.smet.EstimateSmet;
import com.ibcon.sproject.services.crud.CrudService;

import java.util.List;

public interface EstimateHeaderService extends CrudService<EstimateHeader> {
    List<EstimateHeader> findAllBySmetId(Integer smetId);
    List<EstimateHeader> findAllBySmet(EstimateSmet smet);
}
