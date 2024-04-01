package com.example.chattingweb.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// 예외 발생 객체
@Data
@AllArgsConstructor
public class ErrorResult {

    private String code;
    private String message;


}
