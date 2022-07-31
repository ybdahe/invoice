package com.hitpixell.invoice.repository;

import com.hitpixell.invoice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
	Client findById(String id);
	Client findByClientName(String clientName);
}
