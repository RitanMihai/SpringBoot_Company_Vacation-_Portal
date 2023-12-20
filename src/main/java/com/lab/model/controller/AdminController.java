package com.lab.model.controller;

import com.lab.model.model.RoleEntity;
import com.lab.model.model.UserEntity;
import com.lab.model.repository.RoleRepository;
import com.lab.model.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        List<UserEntity> employees = userService.findAll();

        model.addAttribute("employees", employees);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", new UserEntity());
        return "admin-dashboard";
    }

    /* PatchMapping */
    @PostMapping()
    public String updateRole(@RequestParam("userId") Long userId, Model model, @RequestParam("roles") Long ... roles) {
        logger.info("update user called");
        UserEntity user = userService.findById(userId);
        user.getRoles().clear();

        for (Long roleId : roles) {
            RoleEntity role = roleRepository.findById(roleId).orElseThrow(); // Add appropriate error handling
            user.addRole(role);
            userService.save(user);
        }

        return "redirect:/admin";
    }
}
