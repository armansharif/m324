package com.video.modules.video.repository;

import com.video.modules.video.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video,Long> {
    @Query(nativeQuery = true, value = "select * from video ")
    List<Video> getVideos( Pageable pageable);

    List<Video> findAll(Specification<Video> videoSpec, Pageable pageable);


}
