package com.example.chattingweb.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    private int authNumber;

    //임의의 6자리 숫자 반환
    public void makeRandomNumber(){
        Random r = new Random();
        StringBuilder randomNumber = new StringBuilder();
        //10 안의 숫자 임의로 6번 넣어주기
        for(int i=0 ; i<6 ; i++){
//            randomNumber += Integer.toString(r.nextInt(10));
            randomNumber.append(Integer.toString(r.nextInt(10)));
        }
        authNumber = Integer.parseInt(randomNumber.toString());
    }

    // 메일을 어디서 보내는지, 어디로 보내는지, 인증 번호를 html 형식으로 어떻게 보내는지 작성
    public String joinEmail(String email){
        makeRandomNumber();

        String setForm = "lee56244@gmail.com"; // 메일을 보낼 주소
        String toMail = email;                 // 메일을 받을 주소
        String title = "회원가입 인증 이메일 입니다.";
        String content =
                "회원 가입에 감사드립니다.<br><br>" +
                        "인증 번호 : " +authNumber +" <br>";
        mailSender(setForm, toMail, title, content);

        //인증 번호 6자리 반환해줌
        return Integer.toString(authNumber);
    }

    //이메일 전송 메소드
    public void mailSender(String setFrom, String toMail, String title, String content){
        MimeMessage message = mailSender.createMimeMessage(); //MimeMassage 객체 생성

        try{
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");//이메일 메시지와 관련된 설정을 수행합니다.

            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content,true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            mailSender.send(message);
        }catch(MessagingException e){
            //이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            e.printStackTrace();
        }
    }
}


