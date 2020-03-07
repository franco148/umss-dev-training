package com.fral.spring.billing.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fral.spring.billing.models.dao.ClientDao;
import com.fral.spring.billing.models.dao.InvoiceDao;
import com.fral.spring.billing.models.dao.ProductDao;
import com.fral.spring.billing.models.entity.Client;
import com.fral.spring.billing.models.entity.Invoice;
import com.fral.spring.billing.models.entity.Product;

@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	private ClientDao clientDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private InvoiceDao invoiceDao;

	
	@Override
	@Transactional(readOnly = true)
	public List<Client> findAll() {

		return (List<Client>)clientDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Client> findAll(Pageable pageable) {
		
		return clientDao.findAll(pageable);
	}

	@Override
	@Transactional
	public void save(Client client) {
		
		clientDao.save(client);
	}

	@Override
	@Transactional(readOnly = true)
	public Client findOne(Long id) {
		
		return clientDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Client fetchByIdWithInvoices(Long id) {
		return clientDao.fetchByIdWithFacturas(id);
	}

	@Override
	public void delete(Long id) {
		
		clientDao.deleteById(id);
	}

	@Override
	public List<Product> findProductByName(String term) {
		// TODO Auto-generated method stub
		return productDao.findByNameLikeIgnoreCase("%"+term+"%");
	}

	@Override
	@Transactional
	public void saveInvoice(Invoice invoice) {
		// TODO Auto-generated method stub
		invoiceDao.save(invoice);
	}

	@Override
	@Transactional(readOnly=true)
	public Product findProductoById(Long id) {
		// TODO Auto-generated method stub
		return productDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly=true)
	public Invoice findInvoiceById(Long id) {
		// TODO Auto-generated method stub
		return invoiceDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteInvoice(Long id) {
		// TODO Auto-generated method stub
		invoiceDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly=true)
	public Invoice fetchInvoiceByIdWithClientWhithInvoiceItemsWithProduct(Long id) {
		// TODO Auto-generated method stub
		return invoiceDao.fetchByIdWithClientWhithInvoiceItemWithProduct(id);
	}
}
