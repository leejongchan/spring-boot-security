package com.study.spring.springsecurity.configuration.security.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.spring.springsecurity.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Authentication Denied");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(403);
        response.setContentLength(objectMapper.writeValueAsString(errorResponse).length());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
