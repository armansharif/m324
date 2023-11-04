package com.pa.modules.location.controller;

import com.pa.commons.Routes;
import com.pa.modules.location.model.City;
import com.pa.modules.location.model.District;
import com.pa.modules.location.model.State;
import com.pa.modules.location.service.LocationService;
import com.pa.modules.user.model.Users;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(produces = "application/json")
public class LocationController {

    private LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping(value = {Routes.GET_location_city})
    public List<City> getAllCities(@PathVariable(required = false) String stateId, HttpServletResponse response) {
        return locationService.findAllCities(stateId);
    }

    @GetMapping(value = {Routes.GET_location_state})
    public List<State> getAllStates(HttpServletResponse response) {
        return locationService.findAllState();
    }

    @GetMapping(value = {Routes.GET_location_district})
    public List<District> getAllDistrict(@PathVariable(required = false) String cityId, HttpServletResponse response) {
        return locationService.findAllDistrict(cityId);
    }
}
