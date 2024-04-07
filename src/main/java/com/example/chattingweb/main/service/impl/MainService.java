package com.example.chattingweb.main.service.impl;

import com.example.chattingweb.mail.dto.EmailRequestDto;
import com.example.chattingweb.main.dto.UserDto;
import com.example.chattingweb.main.repository.MainRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class MainService {

    @Autowired
    private MainRepository mainRepository;

    public int loginCheck(UserDto userDto) { return mainRepository.loginCheck(userDto); }

    public UserDto login(UserDto userDto) { return mainRepository.login(userDto); }

    public int totalCnt(){
        return mainRepository.getCnt();
    }

    public int join(UserDto user) { return mainRepository.insertUser(user);  }

    public int emailCheck(EmailRequestDto emailDto){
        return mainRepository.emailCheck(emailDto);
    }
}
