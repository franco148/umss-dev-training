package com.umss.dev.training.jtemplate.common.dto.request;

import com.umss.dev.training.jtemplate.persistence.domain.RoleEnum;

import javax.validation.constraints.NotNull;

public class RoleRegistrationDto {

    @NotNull
    private RoleEnum authority;


    public RoleEnum getAuthority() {
        return authority;
    }

    public void setAuthority(RoleEnum authority) {
        this.authority = authority;
    }

}
