package com.springboot.biz.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.google.gson.Gson;
import com.springboot.biz.dto.MemberDto;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class APILoginSuccessHandler implements AuthenticationSuccessHandler{
	//로그인에 성공한 후 JSON으로 데이터 생성
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException{
		
		log.info("-----------");
		log.info("검색한 객체: " + authentication);
		log.info("-----------");
		
		MemberDto memberDto = (MemberDto) authentication.getPrincipal();
		
		Map<String,Object> claims = memberDto.getClaims();
		
		claims.put("accessToken", "");
		claims.put("refreshToken", "");
		
		Gson gson = new Gson();
		
		String jsonStr = gson.toJson(claims);
		
		response.setContentType("application/json; charset=UTF-8");
		
		PrintWriter printWriter = response.getWriter();
		printWriter.println(jsonStr);
		printWriter.close();
	
	}

}
