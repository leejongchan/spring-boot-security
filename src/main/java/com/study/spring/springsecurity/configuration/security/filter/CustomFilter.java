package com.study.spring.springsecurity.configuration.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.spring.springsecurity.vo.SampleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomFilter extends OncePerRequestFilter {

    private AuthenticationManager authenticationManager;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * CustomFilter를 SecurityConfig에서 new가 아닌 Autowired으로 주입을 받아야
     * CustomFilter안의 Autowired로 주입받은 객체를 사용할 수 있다.
     * Spring은 new로 생성한 객체 안의 Autowired 객체를 인식할 수 없다.
     *
     * https://stackoverflow.com/questions/19896870/why-is-my-spring-autowired-field-null
     */
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /**
         * Client로 요청받는 객체라고 생각하고 작성하였습니다.
         */
        if(request.getRequestURI().equals("/security")) {
            SampleData sampleData = new SampleData("ID", "PASSWORD", "ROLE_USER");
            //SampleData sampleData = new SampleData("ID", "PASSWORD", "ROLE_USER");

            authenticationManager.authenticate(sampleData);
        }

        filterChain.doFilter(request, response);
    }
}
