package com.example.jpapractice.controller;

import com.example.jpapractice.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    //로그인 페이지로 이동
    @GetMapping("/login")
    public String loginForm(){
        return "login"; // templates/login.html
    }

    @GetMapping("/main")
    public String mainPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model){ //AuthenticationPrincipal 로그인한 사용자의 정보를 꺼내주는 어노테이션
        model.addAttribute("userName",userDetails.getName());
        model.addAttribute("userRole",userDetails.getRole().name());

        return "main"; // templates/main.html
    }

}
