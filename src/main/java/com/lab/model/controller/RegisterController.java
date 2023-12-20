package com.lab.model.controller;

import com.lab.model.model.UserEntity;
import com.lab.model.service.UserService;
import com.lab.model.service.UserValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegisterController {
    private UserService userService;
    private UserValidatorService userValidatorService;

    @Autowired
    public RegisterController(UserService userService, UserValidatorService userValidatorService) {
        this.userService = userService;
        this.userValidatorService = userValidatorService;
    }

    @GetMapping()
    public String open(Model model){
        model.addAttribute("user", new UserEntity());
        return "register"; /* Just load the page, will not call the GET again */
    }

    @PostMapping()
    public String register(@ModelAttribute("user") UserEntity user, BindingResult bindingResult){

        userValidatorService.validate(user, bindingResult);

        if(bindingResult.hasErrors())
            return "register";

        userService.register(user);
        //userService.login(user, authentication);
        return "/login";
    }
}
