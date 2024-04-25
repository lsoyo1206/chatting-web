package com.example.chattingweb.main.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private int userId;
    private String userName;
    private String password;
    private String email;
    private String role;
}

