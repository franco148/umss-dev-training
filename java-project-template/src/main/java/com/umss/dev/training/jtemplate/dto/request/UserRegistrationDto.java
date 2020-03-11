package com.umss.dev.training.jtemplate.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

public class UserRegistrationDto {

    @Email(message = "Email should be a valid e-mail address.")
    @Size(min = 5, max = 50, message = "Email should have at least 5 characters and at most 50.")
    private String email;
    @Size(min = 6, max = 30, message = "Password should contain between 6 and 30 characters.")
    private String password;
    @NotBlank(message = "Name can not be empty.")
    @Size(max = 20, message = "Name must have at most 30 characters.")
    private String name;
    @NotBlank(message = "Last Name can not be empty.")
    @Size(max = 30, message = "Last Name must have at most 30 characters.")
    private String lastName;
    @NotEmpty(message = "At least one role is required.")
    private Set<RoleRegistrationDto> userRoles = new HashSet<>();

    @JsonIgnore
    private Boolean isEnabled = false;
    @JsonIgnore
    private Boolean isDeleted = false;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<RoleRegistrationDto> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<RoleRegistrationDto> userRoles) {
        this.userRoles = userRoles;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
