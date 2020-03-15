package com.umss.dev.training.jtemplate.common.dto.response;

import com.umss.dev.training.jtemplate.common.dto.request.RoleRegistrationDto;

import java.util.Set;

public class UserResponseDto {

    private Long id;
    private String email;
    private String name;
    private String lastName;
    private Set<RoleRegistrationDto> roles;
    private Boolean isEnabled;
    private Boolean isDeleted;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Set<RoleRegistrationDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleRegistrationDto> roles) {
        this.roles = roles;
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
