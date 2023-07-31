package com.dam.modules.dam.repository;

import com.dam.modules.dam.model.Dam;
import com.dam.modules.dam.model.DamStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DamRepository extends JpaRepository<Dam,Long> {
    @Query(nativeQuery = true, value = "select * from dam ")
    List<Dam> getDams(Pageable pageable);

    List<Dam> findAll(Specification<Dam> spec, Pageable pageable);


    @Query(nativeQuery = true, value = "select * from dam ")
    List<Dam> findAllDam(Pageable pageable);
    @Query(nativeQuery = true, value = "select * from dam where users_id =:userId     ")
    List<Dam> findDamByOwner(@Param("userId") String userId, Pageable pageable);



}
