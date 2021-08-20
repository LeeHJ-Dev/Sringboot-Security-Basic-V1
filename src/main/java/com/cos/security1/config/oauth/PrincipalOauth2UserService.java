package com.cos.security1.config.oauth;

import com.cos.security1.config.auth.PrincipalDetail;
import com.cos.security1.config.oauth.provider.FaceBookUserInfo;
import com.cos.security1.config.oauth.provider.GoogleUserInfo;
import com.cos.security1.config.oauth.provider.NaverUserInfo;
import com.cos.security1.config.oauth.provider.OAuth2UserInfo;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public PrincipalOauth2UserService() {
        super();
    }

    /**
     * 1. 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수.
     * 2. 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
     * @param userRequest
     * @return
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest 1= " + userRequest.getClientRegistration());
        System.out.println("userRequest 2= " + userRequest.getAccessToken().getTokenValue());

        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code리턴(OAuth-Client 라이브러리) -> AccessToken 요청
        // userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원프로필 받아준다.
        System.out.println("userRequest 3= " + super.loadUser(userRequest).getAttributes());

        // 회원가입(구글), 로그인
        //userRequest 1= ClientRegistration{registrationId='google', clientId='719385414817-se4umhvp5431cfpkn1qrs9k6eil11kt1.apps.googleusercontent.com', clientSecret='omkXNZx3naAOlBaw_FY4W1hz', clientAuthenticationMethod=org.springframework.security.oauth2.core.ClientAuthenticationMethod@4fcef9d3, authorizationGrantType=org.springframework.security.oauth2.core.AuthorizationGrantType@5da5e9f3, redirectUri='{baseUrl}/{action}/oauth2/code/{registrationId}', scopes=[email, profile], providerDetails=org.springframework.security.oauth2.client.registration.ClientRegistration$ProviderDetails@fcf4663, clientName='Google'}
        //userRequest 2= ya29.a0ARrdaM-M-QkVUQJkjvIKs81wjnYpkzIFmXZYkHIjcioFEyjiEo576q4_N7L8xM1e8hM1IXuIfdIJBJSkjDF85aDjDrjPWT9bSZ3-z3dVDNxjcVpMx7atNAJXMiPx-jzN2PwjzrWnvDOH-XnlPwpCmTglwQ2yGA
        //userRequest 3= {sub=111780928370941748980, name=이희중, given_name=희중, family_name=이, picture=https://lh3.googleusercontent.com/a/AATXAJzTxCIglkLP4wnq8Lhd1lVWdt9uadNK6YFxPMgC=s96-c, email=xxapara@gmail.com, email_verified=true, locale=ko}


        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")){
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FaceBookUserInfo(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttribute("response"));
        }else{
            System.out.println("우리는 구글과 페이스북만 지원해요 ㅎㅎㅎㅎ");
        }

        //회원정보
        String provider     = oAuth2UserInfo.getProvider();    //google
        String providerId   = oAuth2UserInfo.getProviderId();
        String username     = provider + "_" + providerId; //google_111780928370941748980
        String password     = bCryptPasswordEncoder.encode("1234");
        String email        = oAuth2UserInfo.getEmail();
        String role         = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);
        if(userEntity == null){
            System.out.println("회원가입을 시작합니다.");
            //회원가입
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .provider(provider)
                    .providerId(providerId)
                    .role(role)
                    .build();
            userRepository.save(userEntity);
        }else{
            System.out.println("회원가입이 되어 있습니다. id : " + userEntity.getUsername());
        }

        return new PrincipalDetail(userEntity,oAuth2User.getAttributes());
    }
}
