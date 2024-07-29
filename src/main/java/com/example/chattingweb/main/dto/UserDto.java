package com.example.chattingweb.main.dto;

import lombok.*;

@Data   //getter,setter,toString
@NoArgsConstructor  // 모든 필드를 인자로 받는 생성자를 생성
@AllArgsConstructor //파라미터 없는 기본 생성자를 생성
@Builder            //패턴(builder pattern)을 사용하여 객체를 생성할 수 있게 해줌
public class UserDto extends Pagination {

    private int userId;
    private String userName;
    private String password;
    private String email;
    private String ProfileImage;
    private String role;

}

