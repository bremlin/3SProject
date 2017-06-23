package com.ibcon.factage_web.controllers;

import com.ibcon.factage_web.creators.UserFormModelCreatorImpl;
import com.ibcon.factage_web.domain.Role;
import com.ibcon.factage_web.domain.User;
import com.ibcon.factage_web.services.crud.role.RoleService;
import com.ibcon.factage_web.services.crud.user.UserServiceCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {
    private UserServiceCrud userServiceCrud;
    private RoleService roleService;

    @Autowired
    public void setUserServiceCrud(UserServiceCrud userServiceCrud) {
        this.userServiceCrud = userServiceCrud;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
    @RequestMapping("user/new")
    public String create(Model model) {
        UserFormModelCreatorImpl userFormModelCreator = new UserFormModelCreatorImpl(roleService.listAll());
        userFormModelCreator.getUser().setExpirationDate(new Date());
        model.addAttribute("createUserFormModel", userFormModelCreator);
        return "userform";
    }

    @RequestMapping(value = "user", method = RequestMethod.POST)
    public String save(@ModelAttribute("createUserFormModel") UserFormModelCreatorImpl userFormModelCreatorImpl) {
        User user = userFormModelCreatorImpl.createUser(roleService);
        userServiceCrud.saveOrUpdate(user);
        return "redirect:/user/" + user.getId();
    }

    @RequestMapping("user/{id}")
    public String showUser(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userServiceCrud.getById(id));
        return "usershow";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String listAllUsers(Model model) {
        model.addAttribute("users", userServiceCrud.listAll());
        return "users";
    }

    @RequestMapping("user/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        UserFormModelCreatorImpl userFormModelCreator = new UserFormModelCreatorImpl();
        userFormModelCreator.setUser(userServiceCrud.getById(id));
        userFormModelCreator.setAllRoles((List<Role>) roleService.listAll());
        userFormModelCreator.getUser().setExpirationDate(new Date());
        model.addAttribute("createUserFormModel", userFormModelCreator);
        return "userform";
    }

    @RequestMapping("user/delete/{id}")
    public String delete(@PathVariable Integer id) {
        userServiceCrud.delete(id);
        return "redirect:/users";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){
        return "login";
    }
}
