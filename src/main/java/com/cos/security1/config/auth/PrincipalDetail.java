package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 1. 시큐리티가 /login 주소요청이 오면 낚아채서 로그인을 진행시킨다.
 * 2. 로그인을 진행이 완료가 되면 시큐리티 session을 만들어줍니다. (Security ContextHolder)
 * 3. 오브젝트 타입 => Authentication 타입 객체
 * 4. Authentication 안에 User 정보가 있어야 됨.
 * 5. User 오브젝트 타입 => UserDetails
 */

@Data
public class PrincipalDetail implements UserDetails, OAuth2User {

    private User user;
    private Map<String,Object> attributes;

    //일반로그인
    public PrincipalDetail(User user) {
        this.user = user;
    }

    //OAuth 로그인
    public PrincipalDetail(User user, Map<String,Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    //해당 User의 권한이
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
