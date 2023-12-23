package com.lab.model.controller;

import com.lab.model.model.RoleEntity;
import com.lab.model.model.UserEntity;
import com.lab.model.repository.RoleRepository;
import com.lab.model.service.UserService;
import com.lab.model.util.Icon;
import com.lab.model.util.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    //@PreAuthorize("hasAnyRole('MANAGE_ACCOUNTS')")
    public String open(Model model){
        List<MenuItem> menu = new ArrayList<>();

        MenuItem home = new MenuItem();
        home.setName("Home");
        Icon homeIcon = Icon.HOME;
        homeIcon.setColor(Icon.IconColor.INDIGO);

        home.setIcon(homeIcon);
        home.setUrl("/admin");
        menu.add(home);

        MenuItem roles = new MenuItem();
        roles.setName("Roles");
        roles.setUrl("/admin/roles");
        Icon rolesIcon = Icon.ROLE;
        rolesIcon.setColor(Icon.IconColor.INDIGO);

        roles.setIcon(rolesIcon);
        menu.add(roles);

        model.addAttribute("menuItems", menu);
        return "admin/dashboard";
    }

    @GetMapping
    @RequestMapping("/roles")
    public String openRoles(Model model){
        List<UserEntity> employees = userService.findAll();
        List<MenuItem> menu = new ArrayList<>();

        MenuItem home = new MenuItem();
        home.setName("Home");
        Icon homeIcon = Icon.HOME;
        homeIcon.setColor(Icon.IconColor.INDIGO);

        home.setIcon(homeIcon);
        home.setUrl("/admin");
        menu.add(home);

        MenuItem roles = new MenuItem();
        roles.setName("Roles");
        roles.setUrl("/admin/roles");
        Icon rolesIcon = Icon.ROLE;
        rolesIcon.setColor(Icon.IconColor.INDIGO);

        roles.setIcon(rolesIcon);
        menu.add(roles);

        model.addAttribute("menuItems", menu);
        model.addAttribute("employees", employees);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", new UserEntity());
        return "admin/roles";
    }

    /* PatchMapping */
    @PatchMapping("/roles") /* Partial modification */
    public String updateRole(@RequestParam("userId") Long userId, Model model, @RequestParam("roles") Long ... roles) {
        logger.info("update user called");
        UserEntity user = userService.findById(userId);
        user.getRoles().clear();

        for (Long roleId : roles) {
            RoleEntity role = roleRepository.findById(roleId).orElseThrow(); // Add appropriate error handling
            user.addRole(role);
            userService.save(user);
        }

        return "redirect:/admin/roles";
    }
}
