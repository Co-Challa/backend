package com.cochalla.cochalla.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cochalla.cochalla.dto.LoginRequestDto;
import com.cochalla.cochalla.dto.SignupRequestDto;
import com.cochalla.cochalla.domain.User;
import com.cochalla.cochalla.repository.UserRepository;
import com.cochalla.cochalla.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository user_repository;
    private final PasswordEncoder password_encoder;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDto request){
        if (user_repository.existsByUserId(request.getUserId())){
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        if (user_repository.existsByNickname(request.getNickname())){
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        String encoded_password = password_encoder.encode(request.getPassword());

        User user = User.builder()
        .userId(request.getUserId())
        .nickname(request.getNickname())
        .password(encoded_password)
        .profileImg(request.getProfileImg())
        .resTime(request.getResTime())
        .build();

        user_repository.save(user);
    }

    public String login(LoginRequestDto requestDto){
        User user = user_repository.findById(requestDto.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!password_encoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.createToken(user.getUserId());
    }
}