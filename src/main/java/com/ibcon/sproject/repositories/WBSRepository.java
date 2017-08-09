package com.ibcon.sproject.repositories;

import com.ibcon.sproject.domain.WBS;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WBSRepository extends CrudRepository<WBS, Integer> {
    List<WBS> findAllByProjectId(Integer id);
}
