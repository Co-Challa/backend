package com.cochalla.cochalla.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cochalla.cochalla.entity.User;

import lombok.Getter;

@Getter
public class CustomUserDetail implements UserDetails{
    private final User user;

    public CustomUserDetail(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return Collections.emptyList();
    }

    @Override
    public String getPassword() {
       return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

}
