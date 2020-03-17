package com.umss.dev.training.jtemplate.service;

import com.umss.dev.training.jtemplate.persistence.domain.Role;
import com.umss.dev.training.jtemplate.persistence.domain.User;
import com.umss.dev.training.jtemplate.common.dto.request.UserRegistrationDto;
import com.umss.dev.training.jtemplate.common.dto.response.UserResponseDto;
import com.umss.dev.training.jtemplate.exception.UserNotFoundException;
import com.umss.dev.training.jtemplate.persistence.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private BCryptPasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
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

        User user = userRepository.findById(userId).orElse(null);

        if (null == user) {
            String message = "User with ID=%s could not be found.";
            throw new UserNotFoundException(String.format(message, userId));
        }

        UserResponseDto foundUser = modelMapper.map(user, UserResponseDto.class);

        return foundUser;
    }

    public UserResponseDto save(UserRegistrationDto userDto) {
        User converted = modelMapper.map(userDto, User.class);
        converted.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User persistedUser = userRepository.save(converted);
        UserResponseDto userResponse = modelMapper.map(persistedUser, UserResponseDto.class);

        return userResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userAccount) throws UsernameNotFoundException {

        Optional<User> foundUser = userRepository.findUserByEmail(userAccount);
        if (!foundUser.isPresent()) {
            String errorMessage = "User with account = %s does not exist.";
            logger.error("Login error: " + String.format(errorMessage, userAccount));
            throw new UsernameNotFoundException(String.format(errorMessage, userAccount));
        }

        Set<GrantedAuthority> authorities = getUserAuthorities(userAccount, foundUser.get().getRoles());

        return new org.springframework.security.core.userdetails.User(foundUser.get().getEmail(), foundUser.get().getPassword(), foundUser.get().getEnabled(), true, true, true, authorities);
    }

    //region Utilities
    private Set<GrantedAuthority> getUserAuthorities(String userAccount, Set<Role> userRoles) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : userRoles) {
            logger.info("Role: ".concat(role.getAuthority().name()));
            authorities.add(new SimpleGrantedAuthority(role.getAuthority().name()));
        }

        if (authorities.isEmpty()) {
            String errorMessage = "User with account = %s does not have assigned roles.";
            logger.error("Login error: " + String.format(errorMessage, userAccount));
            throw new UsernameNotFoundException(String.format(errorMessage, userAccount));
        }

        return authorities;
    }
    //endregion
}
