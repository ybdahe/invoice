package com.hitpixell.invoice.repository;

import com.hitpixell.invoice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByOrderId(String orderId);

    List<Transaction> findByClientName(String clientName);

    List<Transaction> findByClientNameAndTransactionDateBetweenOrderByTransactionDateAsc(String clientName, LocalDateTime transactionDateStart, LocalDateTime transactionDateEnd);

    void deleteByClientName(String clientName);
}
