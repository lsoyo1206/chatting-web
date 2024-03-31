package com.example.chattingweb.main.controller;


import com.example.chattingweb.main.dto.UserDto;
import com.example.chattingweb.main.service.impl.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    private MainService mainService;

    @GetMapping("/home")
    public String indexPage(Model model){
        int totalCnt = mainService.totalCnt();
        model.addAttribute("cnt",totalCnt);
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

}
