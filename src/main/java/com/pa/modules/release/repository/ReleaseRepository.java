package com.pa.modules.release.repository;

import com.pa.modules.release.model.ReleaseApps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository extends JpaRepository<ReleaseApps,Long> {
}
