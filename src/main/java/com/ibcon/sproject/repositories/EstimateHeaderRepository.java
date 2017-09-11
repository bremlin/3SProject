package com.ibcon.sproject.repositories;

import com.ibcon.sproject.domain.smet.EstimateHeader;
import com.ibcon.sproject.domain.smet.EstimateSmet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EstimateHeaderRepository extends CrudRepository<EstimateHeader, Integer> {
    List<EstimateHeader> findAllBySmetId(Integer smetId);
    List<EstimateHeader> findAllBySmet(EstimateSmet smet);
}
