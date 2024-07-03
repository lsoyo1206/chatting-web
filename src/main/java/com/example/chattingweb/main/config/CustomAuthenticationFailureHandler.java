package com.example.chattingweb.main.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {   //세밀한 로그인 실패 처리를 구현
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        // 이메일 또는 비밀번호가 잘못 입력되었을 때 실행할 코드를 작성합니다.
        // 예를 들어, request.getParameter()를 사용하여 실패 이유를 확인할 수 있습니다.
        String errorMessage = "이메일 또는 비밀번호가 잘못되었습니다.";
        // 실패 이유를 파라미터로 전달하여 failureUrl("/loginResult?error=true")로 리다이렉트합니다.
        response.sendRedirect("/loginResult?error=true&message=" + errorMessage);

    }
}
