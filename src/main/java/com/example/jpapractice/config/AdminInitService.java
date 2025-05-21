package com.example.jpapractice.config;

import com.example.jpapractice.domain.Role;
import com.example.jpapractice.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitService {

    private final UserService userService;

    @PostConstruct //자바/Spring에서 **"이 클래스가 빈으로 생성된 직후 한 번 실행되는 메서드"**에 붙이는 어노테이션 : 딱 한 번 만!
    public void initAdmin(){
        try {
            userService.saveUser("관리자", "admin@site.com","1234", Role.ADMIN);
            System.out.println("관리자 계정 생성 완료");
        }catch (IllegalStateException e){
            System.out.println("이미 관리자 계정이 존재합니다.");
        }
    }
}
