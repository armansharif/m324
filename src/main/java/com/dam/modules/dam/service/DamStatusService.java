package com.dam.modules.dam.service;

import com.dam.modules.dam.model.DamStatus;
import com.dam.modules.dam.repository.DamStatusRepository;
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
