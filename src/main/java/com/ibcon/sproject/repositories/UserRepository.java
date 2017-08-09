package com.ibcon.sproject.repositories;

import com.ibcon.sproject.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByName(String name);
}
