package com.fral.spring.billing.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fral.spring.billing.models.dao.AuthDao;
import com.fral.spring.billing.models.entity.Role;
import com.fral.spring.billing.models.entity.User;

@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {

	@Autowired
	private AuthDao authDao;
	
	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
        User user = authDao.findByUsername(username);
        
        if(user == null) {
        	logger.error("Login error: User '" + username + "' does not exist in the system!");
        	throw new UsernameNotFoundException("Username: " + username + " does not exist in the system!");
        }
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        for(Role role: user.getRoles()) {
        	logger.info("Role: ".concat(role.getAuthority()));
        	authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        
        if(authorities.isEmpty()) {
        	logger.error("Login error: User '" + username + "' does not have assigned roles!");
        	throw new UsernameNotFoundException("Login error: User '" + username + "' does not have assigned roles!");
        }
        
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getEnabled(), true, true, true, authorities);
	}
}
