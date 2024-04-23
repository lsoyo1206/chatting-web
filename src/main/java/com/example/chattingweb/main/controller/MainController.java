package com.example.chattingweb.main.controller;


import com.example.chattingweb.main.dto.CustomUserDetails;
import com.example.chattingweb.main.dto.UserDto;
import com.example.chattingweb.main.service.impl.MainService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
public class MainController{

    @Autowired
    private MainService mainService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    @GetMapping("/")
    public String indexPage(HttpSession session, Model model){

        //username 임
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        //사용자의 role 값
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authoerities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authoerities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        System.out.println(role);

        if(username != null){
            UserDto userDto = mainService.findByEmail(username);
            model.addAttribute("userDto",userDto);
            session.setAttribute("userDto",userDto);
            System.out.println(userDto);
        }
        return "/main/main";
    }

    @GetMapping("/join")
    public String joinPage(){   return "/main/join";    }

    @GetMapping("/my-page")
    public String mypage(Model model, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");

        UserDto userDto = mainService.findById(userId);
        model.addAttribute("userDto", userDto);

        return "/main/my-page";
    }

//    @ResponseBody
    @PostMapping("/joinProc")
    public String joinProc(@RequestParam("userName") String userName,
                           @RequestParam("loginId") String loginId,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password,
                           RedirectAttributes redirectAttributes){

        UserDto user = new UserDto();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(password);
        user.setLoginId(loginId);

        int result = mainService.join(user);

        if(result == 1){
            redirectAttributes.addFlashAttribute("message", "회원가입에 성공했습니다. 로그인 해주세요");
            return "redirect:/login";
        }else   redirectAttributes.addFlashAttribute("message", "이미 존재하는 이메일 입니다. 다시 시도해 주세요");

        return "redirect:/join";
    }

    @GetMapping("/login")
    public String loginPage(){   return "/main/login";    }

//    @PostMapping("/loginProc")
//    public String loginStart(@RequestParam("email") String email,
//                             @RequestParam("password") String password,
//                             RedirectAttributes redirectAttributes,
//                             HttpServletRequest request){
//
//        if (!email.isEmpty() && !password.isEmpty()) {
//            UserDto userDto = new UserDto();
//            userDto.setEmail(email);
//            userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
//            int result = mainService.loginCheck(userDto);
//            if(result == 1){
//                UserDto user = mainService.login(userDto);
//                return "redirect:/";
//            }else{
//                redirectAttributes.addFlashAttribute("error", "이메일 혹은 비밀번호가 틀렸습니다");
//                return "redirect:/login";
//            }
//        }else{
//            redirectAttributes.addFlashAttribute("error", "이메일 혹은 비밀번호가 모두 적어주세요");
//            return "redirect:/login";
//        }
//    }


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
