package com.lab.model.repository;

import com.lab.model.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    //@Query(nativeQuery = true, name = "SELECT * FROM role WHERE name =: adminRoleName")
    Optional<RoleEntity> findByName(String adminRoleName);
}
