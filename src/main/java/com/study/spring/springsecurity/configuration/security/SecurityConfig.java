package com.study.spring.springsecurity.configuration.security;

import com.study.spring.springsecurity.configuration.security.entrypoint.CustomAuthenticationEntryPoint;
import com.study.spring.springsecurity.configuration.security.filter.CustomFilter;
import com.study.spring.springsecurity.configuration.security.handler.CustomAuthorityHandler;
import com.study.spring.springsecurity.configuration.security.provider.CustomProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private CustomFilter customFilter;

    private CustomProvider customProvider;

    private CustomAuthorityHandler customAuthorityHandler;

    private CustomAuthenticationEntryPoint customAuthenticationHandler;

    /**
     * Autowired을 통한 의존성 주입
     */
    public SecurityConfig(CustomProvider customProvider,
                          CustomAuthorityHandler customAuthorityHandler,
                          CustomAuthenticationEntryPoint customAuthenticationHandler) {
        this.customProvider = customProvider;
        this.customAuthorityHandler = customAuthorityHandler;
        this.customAuthenticationHandler = customAuthenticationHandler;
    }

    /**
     * CustomFilter와 SecurityConfig 사이에 Circle Dependency Issue가 발생하므로
     * 해결 방법들 중 Spring Docs에서 권하는 Setter Injection을 사용한다.
     *
     * https://www.baeldung.com/circular-dependencies-in-spring
     */
    @Autowired
    public void setCustomFilter(CustomFilter customFilter) {
        this.customFilter = customFilter;
    }

    /**
     * 실제 인증 처리를 진행하는 AuthenticationManager를 Bean으로 등록
     * AuthenticationManager의 구현체로 ProviderManager가 있다.
     *
     * ProviderManager가 인증을 직접 처리하는 것이 아니라 Provider에게
     * 위임하여 다른 여러 Provider를 사용해서 처리한다.
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        /**
         * 인증 로직을 처리 할 CustomProvider 등록
         *
         * CustomProvider을 AuthenticationManager에 등록해야 Security Filter Chain 과정 중에서
         * Filter가 AuthenticationManager를 호출하는 경우 CustomProvider이 호출된다.
         */
        auth.authenticationProvider(customProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * /security URI로 요청이 들어온 경우 인증이 필요하도록 설정
         *
         * httpBasic 또는 LoginForm을 이용한 것이 아닌 CustomFilter에서
         * Provider를 직접 요청하므로 설정하지 않아도 무방하다.
         *
         * accessDeniedHandler : 권한 관련 실패가 발생할 경우 처리할 Handler 세팅
         * authenticationEntryPoint : 인증 관련 실패가 발생할 경우 처리할 EntryPoint 세팅
         */
        http.authorizeRequests()
                .antMatchers("/security").authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(customAuthorityHandler)
                .authenticationEntryPoint(customAuthenticationHandler);

        /**
         * 해당 URI에 접근하기 위해 필요한 권한 설정
         *
         * 설정한 String 값에 Spring이 ROLE_ 을 붙여 권한을 설정한다.
         */
        http.authorizeRequests()
                .antMatchers("/security").hasAnyRole("ADMIN");

        /**
         * /nothing URI로 요청이 들어온 경우에는 인증처리를 하지 않는다.
         */
        http.authorizeRequests()
                .antMatchers("/nothing").permitAll();

        /**
         * UsernamePasswordAuthenticationFilter 전에 CustomFilter를 거치도록 한다.
         *
         * 특정 URI에 authenticated를 설정하 LoginForm을 지정해 놓지 않거나
         * httpBasic(기본 인증) 설정을 하지 않은 경우 Provider의 Authenticate Method를 호출하는
         * Filter가 Chain에 등록되어 있지 않아 Provider를 통해 인증을 진행할 수 없다.
         * 따라서, OncePerRequestFilter를 상속받은 CustomFilter를 만들어 Provider를 호출하도록 한다.
         */
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
