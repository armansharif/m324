package com.pa.modules.location.service;

import com.pa.modules.location.model.City;
import com.pa.modules.location.model.District;
import com.pa.modules.location.model.ElectoralDistrict;
import com.pa.modules.location.model.State;
import com.pa.modules.location.repository.CityRepository;
import com.pa.modules.location.repository.DistrictRepository;
import com.pa.modules.location.repository.ElectoralDistrictRepository;
import com.pa.modules.location.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private CityRepository cityRepository;

    private StateRepository stateRepository;

    private DistrictRepository districtRepository;

    private ElectoralDistrictRepository electoralDistrictRepository;

    @Autowired
    public LocationService(CityRepository cityRepository, StateRepository stateRepository, DistrictRepository districtRepository, ElectoralDistrictRepository electoralDistrictRepository) {
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.districtRepository = districtRepository;
        this.electoralDistrictRepository = electoralDistrictRepository;
    }

    public List<City> findAllCities(String stateId) {
        return cityRepository.findCities(stateId);
    }

    public List<District> findAllDistrict(String cityId) {
        return districtRepository.findDistricts(cityId);
    }

    public List<State> findAllState() {
        return stateRepository.findAll();
    }

    public State findStateByDistrict(Long districtId) {
        return stateRepository.getStateOfDistrict(districtId);
    }

    public District findDistrictById(Long districtId) {
        return districtRepository.findById(districtId).orElseGet(null);
    }

//    public ElectoralDistrict findElectoralDistrictByDistrictId(Long districtId) {
//        return el ;
//    }
}
