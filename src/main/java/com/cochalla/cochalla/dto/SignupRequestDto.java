package com.cochalla.cochalla.dto;

import lombok.Data;

@Data
public class SignupRequestDto {
    private String userId;
    private String password;
    private String nickname;
    private Integer profileImg;
    private Integer resTime;
}
