package com.ibcon.factage_web.repositories;

import com.ibcon.factage_web.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByName(String name);
}
