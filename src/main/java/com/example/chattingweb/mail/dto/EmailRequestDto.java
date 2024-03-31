package com.example.chattingweb.mail.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EmailRequestDto {

    //1) @기호를 포함해야 된다
    @Email
    @NotEmpty(message = "이메일을 입력해주세요")
    private String email;
}
