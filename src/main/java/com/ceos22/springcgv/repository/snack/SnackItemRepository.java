package com.ceos22.springcgv.repository;

import com.ceos22.springcgv.domain.snack.SnackItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnackItemRepository extends JpaRepository<SnackItem, Long> {
}