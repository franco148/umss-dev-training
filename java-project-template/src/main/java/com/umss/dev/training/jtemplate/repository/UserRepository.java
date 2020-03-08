package com.umss.dev.training.jtemplate.repository;

import com.umss.dev.training.jtemplate.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
