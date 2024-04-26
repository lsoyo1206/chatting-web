package com.example.chattingweb.api.service;

import com.example.chattingweb.main.dto.UserDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ServerApiServiceIf {

    public String foodSearch(Map<String,Object> param);
    public String foodSearchImage(Map<String,Object> param);
    public UserDto userInfo();
    public Map<String, Object> settingParamsAndInsert(Map<String, Object> param, UserDto userDto) throws IOException;
    public List<Map<String,Object>> settingPostList(UserDto userDto);
    public int insertCollectionTeacher(Map<String,Object> photeUploadMap) throws IOException;
}
