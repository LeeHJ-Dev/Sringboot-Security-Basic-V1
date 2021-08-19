package com.cos.security1.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    public PrincipalOauth2UserService() {
        super();
    }

    /**
     * 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수.
     * @param userRequest
     * @return
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest 1= " + userRequest.getClientRegistration());
        System.out.println("userRequest 2= " + userRequest.getAccessToken());
        System.out.println("userRequest 3= " + super.loadUser(userRequest).getAuthorities());
        return super.loadUser(userRequest);
    }
}
