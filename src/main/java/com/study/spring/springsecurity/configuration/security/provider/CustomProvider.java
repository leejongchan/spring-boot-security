package com.study.spring.springsecurity.configuration.security.provider;

import com.study.spring.springsecurity.vo.SampleData;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CustomProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        /**
         * 실제로는 DB의 데이터와 전달받은 데이터를 비교하여 인증 과정을 진행합니다.
         * 여기서는 임의로 인증을 처리하였습니다.
         */
        boolean isAuthenticated = false;

        if(isAuthenticated) {
            /**
             * authentication.setAuthenticated(true);를 하지 않는 경우 필터 처리 과정 중
             * FilterSecurityInterceptor에서 Provider의 authenticate method를 호출하여
             * Provider가 한번 더 실행된다.
             */
            authentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            /**
             * CustomFilter에서 주입받은 AuthenticationManager은 authenticate method 호출시
             * 등록되어있는 provider들을 순회한다.
             * provider의 authenticate가 null이 아닌 authentication을 return할 경우
             * 다음 provider은 처리 하지 않는 것을 코드 디버깅을 통해 알아냈다.
             */
            return SecurityContextHolder.getContext().getAuthentication();
        }
        else {
            /**
             * 인증 실패한 경우 Credential이 없는 객체를 Return 시
             * 지정한 AuthenticationEntryPoint의 commence method가 호출된다.
             *
             * Throw Exception 을 할 경우 commence method는 호출되지 않는다.
             *
             * https://stackoverflow.com/questions/39512849/spring-boot-spring-security-authenticationentrypoint-not-getting-executed
             */
            return new SampleData();
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
