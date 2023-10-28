package com.pa.modules.location.repository;

import com.pa.modules.location.model.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District,Long> {

    @Query(nativeQuery = true, value = "select * from district where city_id =:cityId     ")
    List<District> findDistricts(@Param("cityId") String cityId);

    Optional<District> findById(Long id);
}
