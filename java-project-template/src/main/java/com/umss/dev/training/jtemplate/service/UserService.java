package com.umss.dev.training.jtemplate.service;

import com.umss.dev.training.jtemplate.domain.User;
import com.umss.dev.training.jtemplate.dto.request.UserRegistrationDto;
import com.umss.dev.training.jtemplate.dto.response.UserResponseDto;
import com.umss.dev.training.jtemplate.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public  Iterable<UserResponseDto> findAll() {
        List<UserResponseDto> allUsersResponse = userRepository.findAll()
                                            .stream()
                                            .sorted(Comparator.comparing(User::getName))
                                            .map(usr -> {
                                                UserResponseDto response = modelMapper.map(usr, UserResponseDto.class);
                                                return response;
                                            })
                                            .collect(Collectors.toList());

        return allUsersResponse;
    }

    public  UserResponseDto findById(Long userId) {
        UserResponseDto foundUser = null;
        User user = userRepository.findById(userId).orElse(null);

        if (null != user) {
            foundUser = modelMapper.map(user, UserResponseDto.class);
        }

        return foundUser;
    }

    public UserResponseDto save(UserRegistrationDto userDto) {
        User converted = modelMapper.map(userDto, User.class);
        User persistedUser = userRepository.save(converted);
        UserResponseDto userResponse = modelMapper.map(persistedUser, UserResponseDto.class);

        return userResponse;
    }
}
