package com.video.modules.release.repository;

import com.video.modules.release.model.ReleaseApps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository extends JpaRepository<ReleaseApps,Long> {
}
