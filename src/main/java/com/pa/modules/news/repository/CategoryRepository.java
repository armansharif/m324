package com.pa.modules.news.repository;

import com.pa.modules.news.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Override
    List<Category> findAll();
}
