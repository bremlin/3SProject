package com.ibcon.sproject.services;

import com.ibcon.sproject.domain.User;

public interface UserService1 {
    Iterable<User> listAllUsers();

    User getUserById(Integer id);

    User save(User user);

    void delete(Integer id);
}
