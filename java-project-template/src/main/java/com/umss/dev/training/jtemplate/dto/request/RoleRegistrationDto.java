package com.umss.dev.training.jtemplate.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umss.dev.training.jtemplate.domain.RoleEnum;

import javax.validation.constraints.NotNull;

public class RoleRegistrationDto {

    @NotNull
    private RoleEnum authority;
    @JsonIgnore
    private Boolean isDeleted = false;


    public RoleEnum getAuthority() {
        return authority;
    }

    public void setAuthority(RoleEnum authority) {
        this.authority = authority;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
