package com.example.chattingweb.api.service;

import com.example.chattingweb.main.dto.UserDto;
import com.example.chattingweb.main.service.impl.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j  //로그
@Service
public class ServerApiService implements ServerApiServiceIf{

    @Autowired
    private MainService mainService;

    @Override
    public String foodSearch(Map<String, Object> param) {
        return null;
    }

    @Override
    public String foodSearchImage(Map<String, Object> param) {
        return null;
    }

    //세션정보로 사용자 정보 가져오기
    @Override
    public UserDto userInfo() {
        UserDto userDto = null;
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); //email
        if (email != null){
            userDto = mainService.findByEmail(email);
            System.out.println(userDto);
        }
        return userDto;
    }

    @Override
    public Map<String, Object> settingParams(Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>();

        log.info("=====================");
        log.info("param:{}", param);
        log.info("param.post:{}", param.get("postDto"));
        log.info("param.space:{}", param.get("spaceDto"));
        log.info("=====================");

        System.out.println("param ===>" + param);
        System.out.println("param.post:"+ param.get("postDto"));
        System.out.println("param.space:"+ param.get("spaceDto"));
        return Map.of();
    }


}
