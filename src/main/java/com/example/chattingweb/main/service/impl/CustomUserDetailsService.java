package com.example.chattingweb.main.service.impl;

import com.example.chattingweb.main.dto.CustomUserDetails;
import com.example.chattingweb.main.dto.UserDto;
import com.example.chattingweb.main.repository.MainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    MainRepository mainRepository;

    //사용자 로그인시 검증을 위해 username 넣어줌
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //username은 login의 input name 같아야함
        System.out.println("loadUserByUsername email ==>" + email);
        UserDto userDto = mainRepository.findByEmail(email);

        if(userDto == null){
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return new CustomUserDetails(userDto);
    }
}
