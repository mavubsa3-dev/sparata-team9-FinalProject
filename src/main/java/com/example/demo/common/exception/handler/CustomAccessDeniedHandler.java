package com.example.demo.common.exception.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.demo.common.exception.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

// 인가 실패 핸들러 -> 로그인 했지만, 권한이 없을 때 동작 403 FORBIDDEN
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws
		IOException{
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);

		Map<String ,Object> errorResponse = new HashMap<>();
		errorResponse.put("status", ErrorCode.FORBIDDEN.getStatus());
		errorResponse.put("code", ErrorCode.FORBIDDEN.getCode());
		errorResponse.put("message", ErrorCode.FORBIDDEN.getMessage());

		String jsonString = objectMapper.writeValueAsString(errorResponse);

		response.getWriter().write(jsonString);
	}
}
