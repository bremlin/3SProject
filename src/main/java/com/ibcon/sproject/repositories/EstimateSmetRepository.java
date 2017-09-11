package com.ibcon.sproject.repositories;

import com.ibcon.sproject.domain.WBS;
import com.ibcon.sproject.domain.smet.EstimateSmet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface EstimateSmetRepository extends CrudRepository<EstimateSmet, Integer> {
    List<EstimateSmet> findByWbsSet_Id(Integer id);
}
