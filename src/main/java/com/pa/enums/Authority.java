package com.pa.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {

    OP_ACCESS_PUBLIC,
    OP_ACCESS_USER,
    OP_ADD_USER,
    OP_EDIT_USER,
    OP_DELETE_USER,
    OP_ACCESS_ADD,
    OP_ACCESS_EDIT,
    OP_ACCESS_DELETE,
    IS_ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
