package com.dam.modules.user.repository;

import com.dam.modules.user.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles,Long> {
    Roles findRolesByName(String name);
}
