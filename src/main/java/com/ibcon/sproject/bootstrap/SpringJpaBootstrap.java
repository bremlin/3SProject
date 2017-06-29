package com.ibcon.sproject.bootstrap;

import com.ibcon.sproject.domain.*;
import com.ibcon.sproject.services.crud.activity.ActivityService;
import com.ibcon.sproject.services.crud.project.ProjectService;
import com.ibcon.sproject.services.crud.user.UserServiceCrud;
import com.ibcon.sproject.services.crud.role.RoleService;
import com.ibcon.sproject.services.crud.wbs.WBSService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class SpringJpaBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private UserServiceCrud userServiceCrud;
    private RoleService roleService;
    private ProjectService projectService;
    private WBSService wbsService;
    private ActivityService activityService;

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

    @Autowired
    public void setWbsService(WBSService wbsService) {
        this.wbsService = wbsService;
    }

    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        loadUsers();
//        loadRoles();
//        assignUsersToUserRole();
//        assignUsersToAdminRole();
//        loadProjects();
//        assignProjectsToRole();
//        loadWBS();
//        loadActivities();
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
        log.info("Saved project" + role.getName());
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleService.saveOrUpdate(adminRole);
        log.info("Saved project" + adminRole.getName());
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

    private void loadActivities() {
        Activity activity = new Activity();
        activity.setProjectObjectId(1);
//        activity.setProjectId(3);
//        activity.setWbsId(1);
        activity.setActivityId("act_id");
        activity.setActivityName("act_name");
        activity.setDuration(5d);
        activity.setDuration(5d);
        activity.setEarlyStartDate(DateTime.now());
        activity.setLateStartDate(DateTime.now());
        activity.setPlannedStartDate(DateTime.now());
        activity.setActualFinishDate(DateTime.now());
        activity.setEarlyFinishDate(DateTime.now());
        activity.setLateFinishDate(DateTime.now());
        activity.setPlannedFinishDate(DateTime.now());
        activity.setTypeId(1);
        activity.setStatusId(1);
        activity.setPercentageCompletion(0);
        activity.setIsCritical(false);
        activity.setWbs(wbsService.getById(7));
        activityService.saveOrUpdate(activity);
    }

    private void loadWBS() {
        WBS wbs = new WBS();
//        wbs.setProjectId(1);
        wbs.setProjectObjectId(1);
        wbs.setName("wbs");
        wbs.setCode("wbs_code");
        wbs.setParentId(0);
        wbs.setIndexNumber(1);
        wbs.setProject(projectService.getById(5));
        wbsService.saveOrUpdate(wbs);
    }
}
