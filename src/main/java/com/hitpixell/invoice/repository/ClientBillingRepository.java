package com.hitpixell.invoice.repository;

import com.hitpixell.invoice.model.ClientBilling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientBillingRepository extends JpaRepository<ClientBilling, Long> {
	List<ClientBilling> findByClientNameOrderByBillingDateDesc(String clientName);
}
