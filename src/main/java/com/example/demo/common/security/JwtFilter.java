package com.example.demo.common.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	public void doFilterInternal(HttpServletRequest request,
								 HttpServletResponse response,
								 FilterChain filterChain)
		throws ServletException, IOException{

		// 헤더에서 토큰 추출
		String token = resolveToken(request);

		// 유효성 검사
		if(token != null && jwtUtil.validateToken(token)){
			Claims claims = jwtUtil.parseClaims(token);
			Long userId = Long.parseLong(claims.getSubject());
			String role = claims.get("role", String.class);

			UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request){
		String bearerToken = request.getHeader("Authorization");
		if(bearerToken != null && bearerToken.startsWith("Bearer ")){
			return bearerToken.substring(7);
		}
		return null;
	}
}
