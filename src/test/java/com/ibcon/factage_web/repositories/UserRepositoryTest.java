package com.ibcon.factage_web.repositories;

import com.ibcon.factage_web.domain.User;
import com.ibcon.factage_web.services.crud.role.RoleService;
import com.ibcon.factage_web.services.crud.user.UserServiceCrud;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    UserServiceCrud userServiceCrud;

    @Before
    public void setUp() throws Exception {
//        User user1 = new User("Vasya", 1, 1, new Date(31052018L), true);
//        User user2 = new User("Anya", 1, 2, new Date(31052015L), false);
//
//        assertNull(user1.getId());
//        assertNull(user2.getId());
//
//        this.userRepository.save(user1);
//        this.userRepository.save(user2);
//
//        assertNotNull(user1.getId());
//        assertNotNull(user2.getId());
    }

    @Test
    public void fetchDataTest() {
//        User userA = userRepository.findByName("Anya");
//
//        assertNotNull(userA);
//        assertEquals((Integer) 2, userA.getGroupRule());
//
//        Iterable<User> users = userRepository.findAll();
//        int count = 0;
//        for (User user : users) {
//            count++;
//        }
//        assertEquals(count, 2);
    }

    @Test
    public void addRoleTest() {
        User user = new User("testTEST", new Date(), true);
        user.setPassword("1");
        user.addRole(roleService.getById(1));
        user.addRole(roleService.getById(2));
        user.addRole(roleService.getById(3));
        userServiceCrud.saveOrUpdate(user);
    }

    @Test
    public void deleteDataTest() {
    }

    @After
    public void tearDown() throws Exception {
    }

}