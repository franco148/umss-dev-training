package com.fral.spring.billing.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.fral.spring.billing.models.entity.Product;

public interface ProductDao extends CrudRepository<Product, Long> {

	@Query("select p from Product p where p.name like %?1%")
	List<Product> findByName(String term);
	
	List<Product> findByNameLikeIgnoreCase(String term);

}
