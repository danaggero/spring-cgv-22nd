package com.ceos22.springcgv.repository;

import com.ceos22.springcgv.domain.purchase.PurchaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseDetailRepository extends JpaRepository<PurchaseDetail, Long> {
}