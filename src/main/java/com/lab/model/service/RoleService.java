package com.lab.model.service;

import com.lab.model.config.util.Role;
import com.lab.model.model.RoleEntity;
import com.lab.model.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void save(RoleEntity ... roles) {
        for (RoleEntity role : roles) {
            roleRepository.save(role);
        }
    }

    public Optional<RoleEntity> findByName(Role role) {
        return this.findByName(role.name());
    }

    public Optional<RoleEntity> findByName(String adminRoleName) {
        return roleRepository.findByName(adminRoleName);
    }
}
