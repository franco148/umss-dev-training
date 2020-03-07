package com.fral.spring.billing.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.fral.spring.billing.models.entity.Client;

public interface ClientDao extends PagingAndSortingRepository<Client, Long> {

	@Query("select c from Client c left join fetch c.invoices i where c.id=?1")
	public Client fetchByIdWithFacturas(Long id);
}
