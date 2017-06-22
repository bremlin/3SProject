package com.ibcon.factage_web.bootstrap;

import com.ibcon.factage_web.domain.Project;
import com.ibcon.factage_web.domain.Role;
import com.ibcon.factage_web.domain.User;
import com.ibcon.factage_web.services.crud.project.ProjectService;
import com.ibcon.factage_web.services.crud.user.UserServiceCrud;
import com.ibcon.factage_web.services.crud.role.RoleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.time.LocalDate;
import java.util.List;

@Component
public class SpringJpaBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private UserServiceCrud userServiceCrud;
    private RoleService roleService;
    private ProjectService projectService;

    private Logger log = Logger.getLogger(SpringJpaBootstrap.class);

    @Autowired
    public void setUserServiceCrud(UserServiceCrud userServiceCrud) {
        this.userServiceCrud = userServiceCrud;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        loadUsers();
//        loadRoles();
//        assignUsersToUserRole();
//        assignUsersToAdminRole();
//        loadProjects();
//        assignProjectsToRole();
    }

    private void loadUsers() {
        User user1 = new User();
        user1.setName("user");
        user1.setPassword("user");
        user1.setExpirationDate(new Date());
        user1.setActive(true);
        userServiceCrud.saveOrUpdate(user1);

        User user2 = new User();
        user2.setName("admin");
        user2.setPassword("admin");
        user2.setExpirationDate(new Date());
        user2.setActive(true);
        userServiceCrud.saveOrUpdate(user2);
    }

    private void loadRoles() {
        Role role = new Role();
        role.setName("USER");
        roleService.saveOrUpdate(role);
        log.info("Saved role" + role.getName());
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleService.saveOrUpdate(adminRole);
        log.info("Saved role" + adminRole.getName());
    }

    private void loadProjects() {
        Project project1 = new Project();
        project1.setProjectObjectId(1);
        project1.setPrimaveraId("prim");
        project1.setProjectName("First Project");
        project1.setProjectDbName("3SWEB");
        project1.setProjectWbsId(1);
        project1.setProjectCode("code");
        project1.setConfigId(1);
        project1.setIndexNumber(1);
        projectService.saveOrUpdate(project1);

        Project project2 = new Project();
        project2.setProjectObjectId(2);
        project2.setPrimaveraId("prim2");
        project2.setProjectName("Second Project");
        project2.setProjectDbName("3SWEB");
        project2.setProjectWbsId(2);
        project2.setProjectCode("code2");
        project2.setConfigId(2);
        project2.setIndexNumber(2);
        projectService.saveOrUpdate(project2);
    }

    private void assignUsersToUserRole() {
        List<Role> roles = (List<Role>) roleService.listAll();
        List<User> users = (List<User>) userServiceCrud.listAll();

        roles.forEach(role -> {
            if (role.getName().equalsIgnoreCase("USER")) {
                users.forEach(user -> {
                    if (user.getName().equals("user")) {
                        user.addRole(role);
                        userServiceCrud.saveOrUpdate(user);
                    }
                });
            }
        });
    }

    private void assignUsersToAdminRole() {
        List<Role> roles = (List<Role>) roleService.listAll();
        List<User> users = (List<User>) userServiceCrud.listAll();

        roles.forEach(role -> {
            if (role.getName().equalsIgnoreCase("ADMIN")) {
                users.forEach(user -> {
                    if (user.getName().equals("admin")) {
                        user.addRole(role);
                        userServiceCrud.saveOrUpdate(user);
                    }
                });
            }
        });
    }

    private void assignProjectsToRole() {
        List<Role> roles = (List<Role>) roleService.listAll();
        List<Project> projects = (List<Project>) projectService.listAll();


        Project project1 = projectService.findByProjectObjectId(1);
        project1.addRole(roles.get(0));
        projectService.saveOrUpdate(project1);

        Project project2 = projectService.findByProjectObjectId(2);
        project2.addRole(roles.get(2));
        projectService.saveOrUpdate(project2);
    }
}
