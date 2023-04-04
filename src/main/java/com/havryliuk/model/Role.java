package com.havryliuk.model;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    PASSENGER,
    MANAGER,
    DRIVER;

    @Override
    public String getAuthority() {
        return name();
    }
}
