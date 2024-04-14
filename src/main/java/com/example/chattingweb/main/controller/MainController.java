package com.example.chattingweb.main.controller;


import com.example.chattingweb.main.dto.UserDto;
import com.example.chattingweb.main.service.impl.MainService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/")
    public String indexPage(HttpSession session, Model model){

        Integer userId = (Integer) session.getAttribute("userId");

        if(userId != null){
            UserDto userDto = mainService.findById(userId);
            model.addAttribute("userDto",userDto);
            System.out.println(userDto);
        }

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
                             HttpServletRequest request){
        System.out.println("userDto = "+userDto);

        int result = mainService.loginCheck(userDto);

        if(result == 1){
            UserDto user = mainService.login(userDto);
            HttpSession session = request.getSession();
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userName", user.getUserName());
//            model.addAttribute("userName", user.getUserName());
            return "redirect:/home";
        }else {
            // 로그인 실패 시 다시 로그인 페이지로 리다이렉트하고 사용자가 입력한 값을 전달합니다.
            redirectAttributes.addFlashAttribute("error", userDto);
            return "redirect:/login";
        }
    }


    @GetMapping("/isLogin")
    @ResponseBody
    public Map<String,Object> isLogin(HttpServletRequest request, Model model) {
        Map<String, Object> response = new HashMap<>();
        String userName = "";
        String userId = "";
        boolean isLogin = false;

        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute("userName") != null){
            isLogin = true;
            userName = String.valueOf(session.getAttribute("userName"));
            userId = String.valueOf(session.getAttribute("userId"));
        }
        response.put("userId", userId);
        response.put("userName", userName);
        response.put("isLogin", isLogin);

        return response;
    }

    @PostMapping("/logout")
    @ResponseBody
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 세션 무효화
        }
        return "success";
    }
}
