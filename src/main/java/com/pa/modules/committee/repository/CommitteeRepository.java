package com.pa.modules.committee.repository;

import com.pa.modules.committee.model.Committee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommitteeRepository extends JpaRepository<Committee, Long> {

    @Override
    public List<Committee> findAll();

    @Query(nativeQuery = true, value = "select * from committee where is_commission = 1")
    List<Committee> getAllCommissions();


    Page<Committee> findAllByIsCommission(int isCommission, Pageable pageable);

    @Override
    Optional<Committee> findById(Long aLong);
}

