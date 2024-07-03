package com.example.chattingweb.api.service;

import com.example.chattingweb.api.dto.PostDto;
import com.example.chattingweb.main.dto.UserDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ServerApiServiceIf {

    public String foodSearch(Map<String,Object> param);
    public String foodSearchImage(Map<String,Object> param);
    public UserDto userInfo();

    public Map<String,Object> sendFileAndInsert(PostDto postDto, UserDto userDto) throws Exception; //file업로드
    public Map<String,Object> insertPostDto(PostDto postDto, UserDto userDto); //post테이블 insert

    public List<Map<String,Object>> settingPostList(UserDto userDto);
    public int insertCollectionTeacher(Map<String,Object> photeUploadMap) throws IOException;
}
