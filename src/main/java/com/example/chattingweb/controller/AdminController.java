package com.example.chattingweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/basic")
public class AdminController {

    @GetMapping("/admin")
    public String indexPage(Model model){
        model.addAttribute("data","init start!!");
        return "/admin/admin";
    }

}
