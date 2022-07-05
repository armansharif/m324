package com.video.modules.user.repository;

import com.video.modules.user.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles,Long> {
    Roles findRolesByName(String name);
}
