package com.ibcon.factage_web.services;

import com.ibcon.factage_web.domain.User;

public interface UserService1 {
    Iterable<User> listAllUsers();

    User getUserById(Integer id);

    User save(User user);

    void delete(Integer id);
}
