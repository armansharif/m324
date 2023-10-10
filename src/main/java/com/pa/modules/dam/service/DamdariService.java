package com.pa.modules.dam.service;

import com.pa.modules.dam.model.Damdari;
import com.pa.modules.dam.repository.DamdariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DamdariService {

    private DamdariRepository damdariRepository;

    @Autowired
    public DamdariService(DamdariRepository damdariRepository) {
        this.damdariRepository = damdariRepository;
    }

    public List<Damdari> findAllDamdari(String sort,
                                        int page,
                                        int perPage) {
        Pageable sortedAndPagination =
                PageRequest.of(page, perPage, Sort.by(sort).ascending());

        return damdariRepository.findAllDamdari(sortedAndPagination);
    }


}
