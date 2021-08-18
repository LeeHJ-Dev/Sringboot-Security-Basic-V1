package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

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
    @GetMapping(value = "/login")
    public String login(){
        System.out.println("IndexController.login");
        return "loginForm";
    }

    @GetMapping(value = "/join")
    public @ResponseBody String join(){
        System.out.println("IndexController.join");
        return "join";
    }

    @GetMapping(value = "/joinProc")
    public @ResponseBody String joinProc(){
        System.out.println("IndexController.joinProc");
        return "회원가입 완료됨";
    }


}
