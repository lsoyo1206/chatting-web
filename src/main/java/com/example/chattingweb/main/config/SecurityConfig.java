package com.example.chattingweb.main.config;

import com.example.chattingweb.main.service.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor   //security 관리
public class SecurityConfig {

    private final CustomUserDetailsService principalDetailService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(principalDetailService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean   //어디서든 사용할 수 있도록  @Authenticationprincipal
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        //anyRequest() : 위에서 처리하지 못한 나머지 경로
        //authenticated() : 로그인한 사용자만 접근가능
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/join", "/login", "/loginProc", "/joinProc", "/user/**").permitAll() //permitAll : 모든 사용자에게 로그인 하지않아도 접근 가능
                        .requestMatchers("/oauth2/kakao/callback", "/result").permitAll()
                        .requestMatchers("/image/v2/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")  //ADMIN role이 있으면 접근 가능
                        .requestMatchers("/user/**", "/api/server/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated())

                //로그인 페이지 경로 설정 -> 로그인 x) admin 경로로 들어갔을 때 오류가 뜨는 것이 아니라 login 페이지로 redirect 해줌
                //loginProcessingUrl() : 로그인 처리를 대신해줌
                //.usernameParameter("email")은 String userName 으로 받는 부분을 email로 변경하겠다는 의미
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/loginResult?error=true")
                        .permitAll());

        //다중 로그인 설정
        //  - maximumSessions : 하나의 아이디에 대한 다중 로그인 허용 개수
        //  - maxSessionPreventLogin : 다중 로그인 개수를 초과하였을 경우 처리 방법 -> true : 초과시 새로운 로그인 차단, false : 초과시 기존 세션 하나 삭제
        http
                .sessionManagement((auth) -> auth.maximumSessions(1).maxSessionsPreventsLogin(true));

        //세션 고정 보호 : 세션 고정 공격을 보호하기 위한 로그인 성공시 세션 설정 방법은 sessionManagement() 메소드의 sessionFixation()으로 설정가능
        //  - none() : 세션 정보변경 x ,  newSession() : 세션 새로 생성 , changeSessionId() : 동일한 세션에 해단 id 변경
        http
                .sessionManagement((auth) -> auth.sessionFixation().changeSessionId());

        // csrf 설정 : 요청을 위조하여 사용자가 원하지 않아도 서버측으로 특정 요청을 강제로 보내는 방식
        http
                .csrf((auth)-> auth.disable());

        //로그아웃
        http.logout((auth) -> auth
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("remember-me")  // 로그아웃 후 삭제할 쿠키 지정
        );

        //자동 로그인 기능(Remember Me) : JSESSIONID이 만료되거나 쿠키가 없을 지라도 어플리케이션이 사용자를 기억하는 기능 (Remember-Me 토큰 쿠키를 이용)
//        http.rememberMe((auth) -> auth
//                .tokenValiditySeconds(3600)       //디폴트 14일
//                .alwaysRemember(false)            // 사용자가 체크박스를 활성화하지 않아도 항상 실행, default: false
//                .userDetailsService(userDetailsService));


        return http.build();
    }

    @Bean //비밀번호의 암호화를 진행할 메소드
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }



}