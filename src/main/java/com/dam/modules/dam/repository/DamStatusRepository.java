package com.dam.modules.dam.repository;

import com.dam.modules.dam.model.DamStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DamStatusRepository extends JpaRepository<DamStatus,Long> {


    @Query(nativeQuery = true, value = "select * from dam_status where dam_id =:damId ")
    List<DamStatus> findAllDamStatus(@Param("damId") String damId, Pageable pageable);
}
