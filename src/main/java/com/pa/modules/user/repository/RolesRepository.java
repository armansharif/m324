package com.pa.modules.user.repository;

import com.pa.modules.user.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles,Long> {
    Roles findRolesByName(String name);
}
