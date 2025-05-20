package com.cochalla.cochalla.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cochalla.cochalla.domain.User;
import com.cochalla.cochalla.repository.UserRepository;
import com.cochalla.cochalla.security.CustomUserDetail;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
        return new CustomUserDetail(user);
    }
}
