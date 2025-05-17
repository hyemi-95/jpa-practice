package com.example.jpapractice.controller;

import com.example.jpapractice.domain.Role;
import com.example.jpapractice.domain.User;
import com.example.jpapractice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // 웹 요청을 처리하는 컨트롤러 클래스
@RequiredArgsConstructor // final로 선언된 필드를 자동 생성자로 주입해주는 Lombok 어노테이션
@RequestMapping("/users") //이 클래스의 모든 URL 앞에 "/users"가 붙음
public class UserController {
    private final UserService userService; //의존성 주입 대상 (UserService를 호출해서 실제 로직 처리 위임)


    // 회원 목록 페이지
    @GetMapping // HTTP GET 요청 중 "/users" 요청 처리
    public String listUsers(Model model){
        List<User> users = userService.findAllUsers(); // 모든 사용자 조회
        model.addAttribute("users",users); // 모델에 데이터 담기 -> 뷰에서 ${users}로 접근 가능
        return "user/list";// 뷰 이름 리턴 -> resources/templates/user/list.html 렌더링
    }

    //회원 등록 폼
    @GetMapping("/new") // "/users/new" 요청 시 사용자 등록 폼 페이지 보여줌
    public String showCreateForm(Model model){
        model.addAttribute("roles", Role.values()); // ENUM 전체 넘기기
        return "user/form"; // templates/user/form.html
    }

    // 회원 저장 처리
    @PostMapping //HTTP POST 요청 "/users" -> 사용자 등록 처리
    public String createUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam Role role
    ){
        userService.saveUser(name, email, role);
        return "redirect:/users"; //저장 후 목록 페이지로 리다이렉트
    }

    // 회원 삭제 처리
    @PostMapping("/delete/{id}") // "/users/delete/1" 요청 시 사용자 삭제 처리
    public String deleteUser(@PathVariable Long id){ // PathVariable : URL 경로에서 값 추출
        userService.deleteUser(id);
        return "redirect:/users"; //목록 페이지로 리다이렉트
    }
}