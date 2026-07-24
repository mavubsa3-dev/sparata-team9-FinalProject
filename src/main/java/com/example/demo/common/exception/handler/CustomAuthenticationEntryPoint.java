package com.example.demo.common.exception.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;

// 인증 실패 핸들러 -> 로그인 x, 토큰 유효하지 않을 때 작동 401 UNAUTHORIZED
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException)
		throws IOException{

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		String message = "로그인이 필요합니다.";
		String jsonString = objectMapper.writeValueAsString(message);

		response.getWriter().write(jsonString);
	}
}
