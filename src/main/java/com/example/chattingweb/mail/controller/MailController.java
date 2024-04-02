package com.example.chattingweb.mail.controller;

import com.example.chattingweb.mail.dto.EmailCheckDto;
import com.example.chattingweb.mail.dto.EmailRequestDto;
import com.example.chattingweb.mail.service.MailService;
import com.example.chattingweb.main.service.impl.MainService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private MainService mainService;

    //사용자에게 메일을 보내는 기능 구현
    // @Valid 이용해 @RequestBody 객체 검증
    // - @Valid 사용시 EmailRequestDto 객체 내의 검증을 수행 - @Email 등등
    @PostMapping("/mailSend")
    @ResponseBody
    public String mailSend(@RequestBody @Valid EmailRequestDto emailDto){
        System.out.print("이메일 인증할 이메일 주소 = " + emailDto.getEmail());
        String message = "SUCCESS";

        int result = mainService.emailCheck(emailDto);
        if(result >= 1){
            message = "Fail";
            return message;
        }

        return mailService.joinEmail(emailDto.getEmail());
    }

    // 사용자 이메일 인증 번호 확인
    @PostMapping("/AuthCheck")
    @ResponseBody
    public String AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto){
        boolean Checked = mailService.CheckAuthNum(emailCheckDto.getEmail(),
                                                    emailCheckDto.getAuthNumber());
        if(Checked){
            return "ok";
        }else{
            throw new NullPointerException("Exception !!");
        }
    }

}
