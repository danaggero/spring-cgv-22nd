package com.ceos22.springcgv.repository;

import com.ceos22.springcgv.domain.Cinema;
import com.ceos22.springcgv.domain.SnackItem;
import com.ceos22.springcgv.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByCinemaAndItem(Cinema cinema, SnackItem item);
}