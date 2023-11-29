package com.example.otzivi.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_UPPER, ROLE_MODERATOR, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
