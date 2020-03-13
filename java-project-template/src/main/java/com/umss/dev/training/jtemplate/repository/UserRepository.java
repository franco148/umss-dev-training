package com.umss.dev.training.jtemplate.repository;

import com.umss.dev.training.jtemplate.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);
}
