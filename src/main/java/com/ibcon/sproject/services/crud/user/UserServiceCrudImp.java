package com.ibcon.sproject.services.crud.user;

import com.ibcon.sproject.creators.user.UserFormModelCreator;
import com.ibcon.sproject.domain.User;
import com.ibcon.sproject.repositories.RoleRepository;
import com.ibcon.sproject.repositories.UserRepository;
import com.ibcon.sproject.services.crud.role.RoleService;
import com.ibcon.sproject.services.encryption.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Profile("springdatajpa")
public class UserServiceCrudImp implements UserServiceCrud {
    private UserRepository userRepository;

    private RoleService roleService;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    private EncryptionService encryptionService;

    @Autowired
    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }


    @Override
    public List<?> listAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public User getById(Integer id) {
        return userRepository.findOne(id);
    }

    @Override
    public User saveOrUpdate(User domainObject) {
        if(domainObject.getPassword() != null){
            domainObject.setEncryptedPassword(encryptionService.encryptString(domainObject.getPassword()));
        }
        return userRepository.save(domainObject);
    }

    public User saveOrUpdate(UserFormModelCreator userFormModelCreator) {
        User newUser = userFormModelCreator.createUser(roleService);
        if (newUser.getId() != null) {
            User oldUser = getById(newUser.getId());
            newUser.setEncryptedPassword(oldUser.getEncryptedPassword());
        }

        return saveOrUpdate(newUser);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        userRepository.delete(id);
    }

    @Override
    public User findByUserName(String username) {
        return userRepository.findByName(username);
    }
}
