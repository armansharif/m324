package com.video.modules.video.service;

import com.video.modules.video.model.LocationPath;
import com.video.modules.video.model.Video;
import com.video.modules.video.repository.LocationPathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class LocationPathService {

    private LocationPathRepository locationPathRepository;

    @Autowired
    public LocationPathService(LocationPathRepository locationPathRepository) {
        this.locationPathRepository = locationPathRepository;
    }


    public LocationPath addLocationPath(LocationPath locationPath) {
        return this.locationPathRepository.save(locationPath);
    }


}
