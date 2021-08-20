package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 1. 시큐리티 설정에서 loginProcessingUrl("/login");
 * 2. /login 요청이 오면 자동으로 UserDetailsService 타입의 IoC되어 있는 loadUserByName 함수가 실행
 */
@Service
public class PrincipalDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 1. 시큐리티 session = Authentication = UserDetails
     * => session(내부 Authentication(내부 UserDetails) )
     * 2. 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //사용자정보조회
        User userEntity = userRepository.findByUsername(username);
        if(userEntity!=null){
            return new PrincipalDetail(userEntity);
        }
        return null;
    }
}
