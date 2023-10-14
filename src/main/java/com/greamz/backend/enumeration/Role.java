package com.greamz.backend.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN,
    MANAGER,
    EMPLOYEE,
    GAME_DEVELOPER;

    @Override
    public String getAuthority() {
        return name();
    }
}
