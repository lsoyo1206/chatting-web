package com.example.chattingweb.main.service.impl;

import com.example.chattingweb.main.dto.UserDto;
import com.example.chattingweb.main.repository.MainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {

    @Autowired
    private MainRepository mainRepository;

    public int totalCnt(){
        return mainRepository.getCnt();
    }

    public int join(UserDto user) { return mainRepository.insertUser(user);  }
}
