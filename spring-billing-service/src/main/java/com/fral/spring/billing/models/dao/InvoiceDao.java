package com.fral.spring.billing.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fral.spring.billing.models.entity.Invoice;

public interface InvoiceDao extends CrudRepository<Invoice, Long> {

	@Query("select i from Invoice i join fetch i.client c join fetch i.invoiceItems l join fetch l.product where i.id=?1")
	Invoice fetchByIdWithClientWhithInvoiceItemWithProduct(Long id);
}
