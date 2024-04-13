package com.example.chattingweb.main.repository;

import com.example.chattingweb.mail.dto.EmailRequestDto;
import com.example.chattingweb.main.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MainRepository {

    public UserDto login(UserDto userDto);

    public int loginCheck(UserDto userDto);

    public int getCnt();

    public int insertUser(UserDto user);

    public int emailCheck(EmailRequestDto emailDto);

    public UserDto findById(int userId);
}
