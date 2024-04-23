package com.example.chattingweb.main.dto;

import lombok.Data;

@Data
public class UserDto {

    private int userId;
    private String loginId;
    private String userName;
    private String password;
    private String email;
    private String role;
}

