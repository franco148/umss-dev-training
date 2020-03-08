package com.umss.dev.training.jtemplate.service;

import com.umss.dev.training.jtemplate.domain.User;
import com.umss.dev.training.jtemplate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public  Iterable<User> findAll() {
        return userRepository.findAll()
                             .stream()
                             .sorted(Comparator.comparing(User::getName))
                             .collect(Collectors.toList());
    }

    public  User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
