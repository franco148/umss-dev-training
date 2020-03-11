package com.umss.dev.training.jtemplate.controller;

import com.umss.dev.training.jtemplate.domain.Role;
import com.umss.dev.training.jtemplate.domain.User;
import com.umss.dev.training.jtemplate.dto.request.RoleRegistrationDto;
import com.umss.dev.training.jtemplate.dto.request.UserRegistrationDto;
import com.umss.dev.training.jtemplate.dto.response.UserResponseDto;
import com.umss.dev.training.jtemplate.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserRestController {

//    @Autowired
    private UserService service;
    private ModelMapper modelMapper;


    public UserRestController(UserService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

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
    public ResponseEntity<UserResponseDto> save(@Valid @RequestBody final UserRegistrationDto userDto) {

        UserResponseDto persistedUser = service.save(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(persistedUser);
    }
}
