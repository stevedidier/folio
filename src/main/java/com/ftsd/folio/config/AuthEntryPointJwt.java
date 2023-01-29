package com.ftsd.folio.config;


import java.io.IOException;
import org.apache.log4j.Logger;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;



@Component
@RequiredArgsConstructor
public class AuthEntryPointJwt implements AuthenticationEntryPoint{

    private static final  Logger logger = Logger.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException{
        logger.error("Unauthorized error: {}", authException);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
    
}
