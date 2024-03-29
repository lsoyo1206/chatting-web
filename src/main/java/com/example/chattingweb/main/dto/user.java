package com.example.chattingweb.main.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Data
@Component
public class user {

    private Long userId;
    private String userName;
    private String password;
    private String email;
}

