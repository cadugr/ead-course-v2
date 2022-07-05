package com.ead.course.configs.security;


import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthenticationJwtFilter extends OncePerRequestFilter {
	
	@Autowired
	JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			//primeiro, obtemos o token da requisição
			String jwtStr = getTokenHeader(request);
			if(jwtStr != null && jwtProvider.validateJwt(jwtStr)) {
				String userId = jwtProvider.getSubjectJwt(jwtStr);
				String rolesStr = jwtProvider.getClaimNameJwt(jwtStr, "roles");
				UserDetails userDetails = UserDetailsImpl.build(UUID.fromString(userId), rolesStr);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null , userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			logger.error("Cannot set User Authentication: {}", e);
		}
		
		filterChain.doFilter(request, response);
		
	}

	private String getTokenHeader(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}
		return null;
	}

}
