package com.study.spring.springsecurity.vo;


import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * 인증에 사용되는 객체로 Authentication을 implements한다.
 */
@NoArgsConstructor
public class SampleData implements Authentication {

    String id;

    String password;

    boolean isAuthenticated;

    String type;

    public SampleData(String id, String password, String type) {
        this.id = id;
        this.password = password;
        this.isAuthenticated = false;
        this.type = type;
    }

    /**
     * 해당 Instance가 가지고 있는 권한을 Return 하는 Method 권한로 확인 처리하는 부분에서 호출된다.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        LinkedList<GrantedAuthority> authorityLinkedList = new LinkedList<>();
        authorityLinkedList.add(new SimpleGrantedAuthority(type));
        return Collections.unmodifiableList(authorityLinkedList);
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    @Override
    public Object getDetails() {
        return this;
    }

    @Override
    public Object getPrincipal() {
        return this.id;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return null;
    }
}
