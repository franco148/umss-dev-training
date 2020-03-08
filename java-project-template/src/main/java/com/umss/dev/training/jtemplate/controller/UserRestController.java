package com.umss.dev.training.jtemplate.controller;

import com.umss.dev.training.jtemplate.domain.User;
import com.umss.dev.training.jtemplate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<Iterable<User>> findAll() {
        Iterable<User> usersResponse = service.findAll();
        return ResponseEntity.ok(usersResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") Long id) {
        User userResponse = service.findById(id);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        User userResponse = service.save(user);
        return ResponseEntity.ok(userResponse);
    }
}
