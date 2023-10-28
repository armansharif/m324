package com.pa.modules.location.repository;

import com.pa.modules.location.model.ElectoralDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectoralDistrictRepository extends JpaRepository<ElectoralDistrict,Long> {


}
