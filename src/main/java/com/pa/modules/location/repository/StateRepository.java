package com.pa.modules.location.repository;


import com.pa.modules.location.model.State;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    @Query(nativeQuery = true, value = " SELECT * from state where state_id in ( select state_id from city where city_id in ( select city_id from district_id = :districtId ))")
    State getStateOfDistrict(  @Param("districtId") Long districtId );
}
