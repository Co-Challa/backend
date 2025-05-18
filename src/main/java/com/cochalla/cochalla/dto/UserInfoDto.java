package com.cochalla.cochalla.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoDto {
    private String userId;
    private String nickname;
    private Integer profileImg;
    private Integer resTime;
}