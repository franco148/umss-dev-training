package com.fral.spring.billing.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.fral.spring.billing.models.entity.User;

public interface AuthDao extends CrudRepository<User, Long> {

	User findByUsername(String username);
}
