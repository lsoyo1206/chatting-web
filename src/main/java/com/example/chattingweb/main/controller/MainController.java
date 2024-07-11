package com.example.chattingweb.main.controller;


import com.example.chattingweb.api.common.ApiExplorer;
import com.example.chattingweb.api.service.ServerApiService;
import com.example.chattingweb.main.dto.CustomUserDetails;
import com.example.chattingweb.main.dto.UserDto;
import com.example.chattingweb.main.service.impl.MainService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private ServerApiService serverApiService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/join")
    public String joinPage(){   return "main/join";    }

    @GetMapping("/login")
    public String loginPage() {     return "main/login";       }

    @GetMapping("/map.do")
    public String map() {     return "main/map";       }

    @GetMapping("/")
    public String indexPage(HttpSession session, Model model){

        String email = SecurityContextHolder.getContext().getAuthentication().getName(); //email

        //사용자의 role 값
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authoerities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authoerities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        System.out.println(role);
        System.out.println("email =>"+email);


        if(email != null){
            UserDto userDto = mainService.findByEmail(email);
            model.addAttribute("userDto",userDto);
            session.setAttribute("userDto",userDto);
            System.out.println(userDto);
        }



        return "main/main";
    }

    @GetMapping("/user/my-page")
    public String mypage(Model model){
        UserDto userDto = serverApiService.userInfo();
        model.addAttribute("userDto", userDto);
        return "user/my-page";
    }

    @PostMapping("/joinProc")
    public String joinProc(@RequestParam("userName") String userName,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password,
                           RedirectAttributes redirectAttributes){

        UserDto userDto = new UserDto();
        userDto.setUserName(userName);
        userDto.setEmail(email);
        userDto.setPassword(password);

        int result = mainService.join(userDto);

        if(result == 1){
            redirectAttributes.addFlashAttribute("message", "회원가입에 성공했습니다. 로그인 해주세요");
            return "redirect:/login";
        }else   redirectAttributes.addFlashAttribute("message", "이미 존재하는 이메일 입니다. 다시 시도해 주세요");

        return "redirect:/join";
    }

    @GetMapping("/loginResult")
    public String loginResult(@RequestParam(name = "error", required = false) String error,
                            Model model) {

        System.out.printf("login error ===>"+error);

        if("true".equals(error)){
            String errorMessage = "이메일 또는 비밀번호가 올바르지 않습니다.";
            model.addAttribute("error", errorMessage);
        }

        return "main/login";
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

    @RequestMapping("/oauth2/kakao/callback")
    public String kakaoLogin(@RequestParam String code){
        // 1.인가 코드 받기 : @RequestParam String code

        //2. 토큰 받기 https://innovation123.tistory.com/181#4.%20KakaoApi.getAccessToken(String%20code)-1
//        String accessTokent = kakaoLogin.getS
        return "result";
    }


//    @PostMapping("/logout")
//    @ResponseBody
//    public String logout(HttpServletRequest request){
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.invalidate(); // 세션 무효화
//        }
//        return "success";
//    }

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
}
