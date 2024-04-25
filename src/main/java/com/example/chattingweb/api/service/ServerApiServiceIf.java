package com.example.chattingweb.api.service;

import com.example.chattingweb.main.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface ServerApiServiceIf {

    public String foodSearch(Map<String,Object> param);
    public String foodSearchImage(Map<String,Object> param);
    public UserDto userInfo();
    public Map<String, Object> settingParamsAndInsert(Map<String, Object> param, UserDto userDto);
    public List<Map<String,Object>> settingPostList(UserDto userDto);
}
