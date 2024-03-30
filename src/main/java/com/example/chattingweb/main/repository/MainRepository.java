package com.example.chattingweb.main.repository;

import com.example.chattingweb.main.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainRepository {

    public int getCnt();

    public int insertUser(UserDto user);
}
