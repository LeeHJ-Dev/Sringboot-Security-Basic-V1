package com.cos.security1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity          // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됩니다.
//@EnableGlobalMethodSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


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
                .loginPage("/login");


    }
}
