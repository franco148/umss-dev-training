package com.umss.dev.training.jtemplate.controller;

import com.umss.dev.training.jtemplate.dto.request.UserRegistrationDto;
import com.umss.dev.training.jtemplate.dto.response.UserResponseDto;
import com.umss.dev.training.jtemplate.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private UserService service;


    public UserRestController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Iterable<UserResponseDto>> findAll() {
        Iterable<UserResponseDto> usersResponse = service.findAll();
        return ResponseEntity.ok(usersResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable("id") Long id) {
        UserResponseDto userResponse = service.findById(id);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> save(@Valid @RequestBody final UserRegistrationDto userDto) {

        UserResponseDto persistedUser = service.save(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(persistedUser);
    }
}
