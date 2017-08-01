package com.ibcon.sproject.repositories;

import com.ibcon.sproject.domain.Link;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LinkRepository extends CrudRepository<Link, Integer> {
    List<Link> findAllByActivity_Id(Integer activity_id);
}
