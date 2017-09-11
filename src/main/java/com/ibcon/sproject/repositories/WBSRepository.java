package com.ibcon.sproject.repositories;

import com.ibcon.sproject.domain.WBS;
import com.ibcon.sproject.domain.smet.EstimateSmet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WBSRepository extends CrudRepository<WBS, Integer> {
    List<WBS> findAllByProjectId(Integer id);

//    @Query("SELECT w.smets FROM WBS w WHERE w.id = :id")
//    List<EstimateSmet> findEstimateSmetsByWbsId(@Param("id") int id);
}
