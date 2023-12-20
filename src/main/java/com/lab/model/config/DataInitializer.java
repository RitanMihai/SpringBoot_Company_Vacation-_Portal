package com.lab.model.config;

import com.lab.model.config.util.Role;
import com.lab.model.model.RoleEntity;
import com.lab.model.model.UserEntity;
import com.lab.model.service.RoleService;
import com.lab.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/***
 * This class initialize the default roles and accounts in the database.
 * Default roles:
 * - INACTIVE
 * - AUTH
 * - APPROVE_DAYS
 * - APPROVE_DAYS_OFF_REQUEST
 * - MANAGE_ACCOUNTS
 * - SET_VACANCY_DAYS_NUMBER
 * Default accounts:
 * - ADMIN has [MANAGE_ACCOUNTS] rights
 */
@Component
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    private final String adminEmail = "admin@admin.com";
    private final Role adminRole = Role.MANAGE_ACCOUNTS;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean adminExists = userService.findByEmail(adminEmail).isPresent();
        boolean rolesExist = roleService.findByName(adminRole).isPresent();

        boolean insertConditions = adminExists && rolesExist;

        if(insertConditions)
            return;

        /* Insert */
        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword("pass");
        admin.setEmail(adminEmail);

        /* Default roles */
        for (Role value : Role.values()) {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setName(value.name());
            roleService.save(roleEntity);
        }

        Optional<RoleEntity> optAdminRole = roleService.findByName(Role.MANAGE_ACCOUNTS);

        RoleEntity roleAdmin = optAdminRole.orElseThrow(() -> new Exception("Role MANAGE_ACCOUNTS does not exists"));

        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleAdmin);

        admin.setRoles(roles);
        userService.register(admin);
    }
}
