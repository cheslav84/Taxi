package com.havryliuk.service;

import com.havryliuk.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserResource {
    User save(User user);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
