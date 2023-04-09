package com.havryliuk.model;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    PASSENGER,
    DRIVER,
    MANAGER;

    @Override
    public String getAuthority() {
        return name();
    }
}
