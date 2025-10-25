package com.ceos22.springcgv.repository.snack;

import com.ceos22.springcgv.domain.cinema.Cinema;
import com.ceos22.springcgv.domain.snack.SnackItem;
import com.ceos22.springcgv.domain.snack.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByCinema(Cinema cinema);
    Optional<Inventory> findByCinemaAndItem(Cinema cinema, SnackItem item);
}