package com.example.chattingweb.main.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainRepository {

    public int getCnt();

    public int join();
}
