package com.example.demo.domain.payment.repository;

import com.example.demo.domain.payment.entity.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    Optional<Settlement> findBySettlementDate(LocalDate settlementDate);

    List<Settlement> findAllByOrderBySettlementDateDesc();
}