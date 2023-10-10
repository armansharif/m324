package com.pa.modules.dam.service;

import com.pa.modules.dam.model.DamStatus;
import com.pa.modules.dam.repository.DamStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DamStatusService {

    private DamStatusRepository damStatusRepository;

    @Autowired
    public DamStatusService(DamStatusRepository damStatusRepository) {
        this.damStatusRepository = damStatusRepository;
    }

    public DamStatus addDamStatusPath(DamStatus damStatus) {
        return this.damStatusRepository.save(damStatus);
    }

}
