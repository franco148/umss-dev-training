package com.umss.dev.training.jtemplate.service;

import com.umss.dev.training.jtemplate.domain.User;
import com.umss.dev.training.jtemplate.dto.request.UserRegistrationDto;
import com.umss.dev.training.jtemplate.dto.response.UserResponseDto;
import com.umss.dev.training.jtemplate.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public  Iterable<User> findAll() {
        return userRepository.findAll()
                             .stream()
                             .sorted(Comparator.comparing(User::getName))
                             .collect(Collectors.toList());
    }

    public  User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public UserResponseDto save(UserRegistrationDto userDto) {
        User converted = modelMapper.map(userDto, User.class);
        User persistedUser = userRepository.save(converted);
        UserResponseDto userResponse = modelMapper.map(persistedUser, UserResponseDto.class);

        return userResponse;
    }
}
