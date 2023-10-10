package com.pa.modules.ticketing.repository;

import com.pa.modules.ticketing.model.TicketCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketCategoryRepository extends JpaRepository<TicketCategory, Long> {
    @Override
    List<TicketCategory> findAll();
}
