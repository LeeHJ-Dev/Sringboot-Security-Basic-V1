package com.cos.security1.controller;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import com.cos.security1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 메인화면 호출하는 경우 기본경로 셋팅한다.
     * Path. Http://localhost:8000/ or Http://localhost:8000 호출
     * @return /src/main/resources/templates/index.mustache file return
     */
    @GetMapping(value = {"","/"})
    public String index(){
        System.out.println("IndexController.index");

        //머스테치 기본폴더 src/main/resources/
        //뷰리졸버(@Controller) 설정 : templates(prefix), .mustache(suffix)
        return "index";
    }

    /**
     *
     * @return
     */
    @GetMapping(value = "/user")
    public @ResponseBody String user(){
        System.out.println("IndexController.user");
        return "user";
    }

    @GetMapping(value = "/admin")
    public @ResponseBody String admin(){
        System.out.println("IndexController.admin");
        return "admin";
    }

    @GetMapping(value = "/manager")
    public @ResponseBody String manager(){
        System.out.println("IndexController.manager");
        return "manager";
    }

    /**
     * 사용자가 인증이 필요한 페이지에 접근하면 Spring Security Configure Filter 하여 로그인 페이지폼으로 이동한다.
     * Path. Http://localhost:8080/{인증이필요한페이지주소..}
     * @return Http://localhost:8080/resources/templates/loginForm.html file return
     */
    @GetMapping(value = "/loginForm")
    public String loginForm(){
        System.out.println("IndexController.login");
        return "loginForm";
    }

    /**
     * 사용자가 회원가입을 진행하는 경우 회원정보 입력 폼을 제공한다.
     * Path. Http://localhost:8080/joinForm
     * @return Http://localhost:8080/resource/templates/joinForm.html file return
     */
    @GetMapping(value = "/joinForm")
    public String joinForm(){
        System.out.println("IndexController.join");
        return "joinForm";
    }

    /**
     * 회원가입페이지에서 회원정보을 입력받아 회원가입을 진행한다.
     * Path. Http://localhost:8080/joinForm -> PostMapping action /join -> 회원가입서비스 호출
     * Password 1234 입력하면 시큐리티로 로그인을 할 수 없음. 이유는 패스워드가 암호화가 안되어 있어서
     * @return
     */
    @PostMapping(value = "/join")
    public String join(User user){
        System.out.println("IndexController.join");
        System.out.println("user = " + user);
        //userService.회원가입(user);

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);

        user.setPassword(encPassword);
        user.setRole("ROLE_USER");
        userRepository.save(user);

        return "redirect:/loginForm";
    }

    @Secured(value = {"ROLE_ADMIN"})
    @GetMapping(value = "/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize(value = "hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/data")
    public @ResponseBody String data(){
        return "데이터";
    }

}
