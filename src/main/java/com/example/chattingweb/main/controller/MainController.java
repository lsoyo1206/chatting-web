package com.example.chattingweb.main.controller;


import com.example.chattingweb.main.dto.UserDto;
import com.example.chattingweb.main.service.impl.MainService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private MainService mainService;

    @GetMapping("/home")
    public String indexPage(Model model){
        model.addAttribute("data","init start!!");
        return "/main/main";
    }

    @GetMapping("/join")
    public String joinPage(){   return "/main/join";    }

    @PostMapping("/register")
    @ResponseBody
    public int userRegister(@RequestBody UserDto user){
        int result = mainService.join(user);
        return result;
    }

    @GetMapping("/login")
    public String loginPage(){   return "/main/login";    }

    @PostMapping("/login")
    public String loginStart(UserDto userDto,
                             RedirectAttributes redirectAttributes,
                             HttpServletResponse response){
        System.out.println("userDto = "+userDto);

        int result = mainService.loginCheck(userDto);

        if(result == 1){
            UserDto user = mainService.login(userDto);
            Cookie cookie = new Cookie("username", user.getUserName());
            cookie.setMaxAge(60 * 60 * 24 * 30); // 쿠키 유효기간을 30일로 설정합니다.
            cookie.setPath("/"); // 쿠키의 경로를 "/"로 설정합니다. 이렇게 하면 전체 애플리케이션에서 쿠키에 접근할 수 있습니다.
            response.addCookie(cookie);
            return "redirect:/home";
        }else {
            // 로그인 실패 시 다시 로그인 페이지로 리다이렉트하고 사용자가 입력한 값을 전달합니다.
            redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
            redirectAttributes.addFlashAttribute("userDto", userDto);
            return "redirect:/login";
        }
    }


    @GetMapping("/isLogin")
    @ResponseBody
    public Map<String,Object> isLogin(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        Map<String, Object> response = new HashMap<>();
        String userName = "";
        boolean isLogin = false;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    userName = cookie.getValue();
                    isLogin = true;
                    System.out.println("Your cookie value: " + userName);
                    break;
                }
            }
        }

        response.put("userName", userName);
        response.put("isLogin", isLogin);
        model.addAttribute("isLogin", isLogin);

        return response;
    }
}
