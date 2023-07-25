package com.dam.modules.video.service;

import com.dam.modules.video.model.LocationPath;
import com.dam.modules.video.repository.LocationPathRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
