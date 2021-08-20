package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * google client id : 719385414817-se4umhvp5431cfpkn1qrs9k6eil11kt1.apps.googleusercontent.com
 * google client pass : omkXNZx3naAOlBaw_FY4W1hz
 *
 * facebook 앱 : 374481084238770
 * facebook 시크릿 코드 : 82a7c36f1ba336555216a48e6040a0bf
 */
@Configuration
@EnableWebSecurity          // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됩니다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalDetailsService;

    //해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encoderPwd(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeRequests()
                //Path. /user/** -> 인증이 필요.
                .antMatchers("/user/**").authenticated()
                //Path /manager/** -> 권한 ROLE_ADMIN or ROLE_MANAGER
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                //Path /admin/** -> 권한 ROLE_ADMIN
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                //Path 위 주소이외에는 접근을 허용한다.
                .anyRequest().permitAll()

                .and()
                .formLogin()
                .loginPage("/loginForm")
                // /login 주소로 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줍니다.
                // Controller "/login" 컨트롤러 생성안해도 된다.
                // 시큐리티가 자체적으로 로그인을 구현한다.
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")

                .and()
                .oauth2Login()
                //구글 로그인이 완료된 뒤의 후처리가 필요함. // 1.코드받기(인증), 2.액세스토큰(권한), 3.사용자프로필 정보를 가져온다.
                //4. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 합니다.
                .loginPage("/loginForm")
                //tip. 코드x,(액세스토큰 + 사용자프로필정보 O )
                .userInfoEndpoint()
                .userService(principalDetailsService)
        ;
    }
}
