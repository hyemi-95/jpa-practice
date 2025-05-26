package com.example.jpapractice.controller;

import com.example.jpapractice.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    //로그인 페이지로 이동
    @GetMapping("/login")
    public String loginForm(){
        return "login"; // templates/login.html
    }

    @GetMapping("/main")
    public String mainPage(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "denied", required = false) String denied, Model model){ //AuthenticationPrincipal 로그인한 사용자의 정보를 꺼내주는 어노테이션
        if(userDetails != null) {
            model.addAttribute("userName", userDetails.getName());
            model.addAttribute("userRole", userDetails.getRole().name());
            model.addAttribute("isLoggedIn", true);
        }else {
            model.addAttribute("isLoggedIn", false);
        }
        if("true".equals(denied)){
            model.addAttribute("accessDeniedMsg","관리자 페이지는 관리자만 접근 가능합니다.");
        }
        return "main"; // templates/main.html
    }
    @GetMapping("/logout-success")
    public String logoutSuccess(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(value = "denied", required = false) String denied, Model model){ //AuthenticationPrincipal 로그인한 사용자의 정보를 꺼내주는 어노테이션

        return "logout-success"; // templates/logout-success.html
    }

}
