package com.example.chattingweb.main.config;

import com.example.chattingweb.main.service.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

    @Bean  //어디서든 사용할 수 있도록
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/join","/login","/loginProc","/joinProc","/user/**").permitAll() //permitAll : 모든 사용자에게 로그인 하지않아도 접근 가능
                        .requestMatchers("/admin/**").hasRole("ADMIN")  //ADMIN role이 있으면 접근 가능
                        .requestMatchers("/user/**","/api/server/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated())
                .formLogin(login ->login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/", true)
                        .permitAll());

        //anyRequest() : 위에서 처리하지 못한 나머지 경로
        //authenticated() : 로그인한 사용자만 접근가능

        //로그인 페이지 경로 설정 -> 로그인 x) admin 경로로 들어갔을 때 오류가 뜨는 것이 아니라 login 페이지로 redirect 해줌
        //loginProcessingUrl() : 로그인 처리를 대신해줌
        //.usernameParameter("email")은 String userName 으로 받는 부분을 email로 변경하겠다는 의미


        // csrf(위변조방지설정) 동작하면 csrf 토큰도 보내주어야 로그인이 진행됨
        http
                .csrf((auth)-> auth.disable());

        return http.build();
    }

    @Bean //비밀번호의 암호화를 진행할 메소드
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
