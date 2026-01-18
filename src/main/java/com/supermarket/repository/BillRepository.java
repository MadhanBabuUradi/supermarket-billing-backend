package com.supermarket.repository;

import com.supermarket.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    Optional<Bill> findByBillNumber(String billNumber);

    List<Bill> findByCustomerNameContainingIgnoreCase(String customerName);

    List<Bill> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
