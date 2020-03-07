package com.fral.spring.billing.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fral.spring.billing.models.entity.Client;
import com.fral.spring.billing.models.entity.Invoice;
import com.fral.spring.billing.models.entity.Product;

public interface ClientService {

	List<Client> findAll();

	Page<Client> findAll(Pageable pageable);

	void save(Client client);

	Client findOne(Long id);
	
	Client fetchByIdWithInvoices(Long id);

	void delete(Long id);

	List<Product> findProductByName(String term);

	void saveInvoice(Invoice invoice);

	Product findProductoById(Long id);

	Invoice findInvoiceById(Long id);

	void deleteInvoice(Long id);

	Invoice fetchInvoiceByIdWithClientWhithInvoiceItemsWithProduct(Long id);
}
