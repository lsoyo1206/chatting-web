package com.example.chattingweb.main.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//오류 발생시 오류 페이지로 이동
@Slf4j
@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 404");
        return "error-page/404";
    }
//    @RequestMapping("/error-page/500")
//    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
//        log.info("errorPage 500");
//        return "error-page/500";
//    }

}
