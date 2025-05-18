package com.example.jpapractice.controller;

import com.example.jpapractice.domain.Role;
import com.example.jpapractice.domain.User;
import com.example.jpapractice.dto.UserCreateDto;
import com.example.jpapractice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        model.addAttribute("userCreateDto", new UserCreateDto()); // ENUM 전체 넘기기
//        model.addAttribute("roles", Role.values()); // ENUM 전체 넘기기
        return "user/form"; // templates/user/form.html
    }

    // 회원 저장 처리
    @PostMapping //HTTP POST 요청 "/users" -> 사용자 등록 처리
    public String createUser(@Valid UserCreateDto form, BindingResult result, Model model)
    //Valid 유효성 검사 트리거 , BindingResult는 유효성 검사를 수행한 객체와 반드시 "세트"로 써야함
    //@Valid(또는 @Validated) 로 유효성 검사를 할 때, 검사 결과(성공/실패)를 받아서 컨트롤러에서 처리하기 위해 사용
    //항상 @Valid 뒤에 BindingResult 사용해야함 : 순서가 바뀌면 Spring이 검사 결과를 result에 넣지 못함
    {
        //1.유효성 검사 실패시
        if(result.hasErrors()){ //모든 필드 오류 체크 , result.hasFieldErrors("email") 형식으로 필드별 에러 추적 가능
//            model.addAttribute("roles",Role.values());
           return  "user/form"; //오류 발생 시 다시 폼으로
            // model.addAttribute("userCreateDto",form)을 안하는 이유:
            // @Valid UserCreateDto form 은 @Valid @ModelAttribute("userCreateDto") UserCreateDto form과 같음 그런데 @ModelAttribute 가 생략되서
            //@Valid UserCreateDto form 자체가  userCreateDto 이라는 이름으로 자동 바인딩 됨
            //즉, 폼에서 넘어온 데이터를 DTO/객체에 바인딩 할때 @ModelAttribute가 사용됨(생략가능)
            //***** 키는 View에 맞추고, 값은 파라미터 변수 그대로
        }
        //2.이메일 중복 예외처리
        try {
            userService.saveUser(form.getName(), form.getEmail(), form.getPassword(), Role.USER); // Dto에 담긴 값, form음 값을 담고있는 변수일 뿐 바인딩에는 사용안됨
        } catch (IllegalStateException e){
            result.rejectValue("email","duplicate", e.getMessage()); // 필드단위 에러 추가
//            model.addAttribute("roles",Role.values());
            return "user/form";
        }
        //3.성공 시
        return "redirect:/users"; //저장 후 목록 페이지로 리다이렉트

        //return "뷰 이름" → 모델 자동 바인딩 된다
        //return "redirect:/경로" → 모델 자동 바인딩 안 된다
    }

    // 회원 삭제 처리
    @PostMapping("/delete/{id}") // "/users/delete/1" 요청 시 사용자 삭제 처리
    public String deleteUser(@PathVariable Long id){ // PathVariable : URL 경로에서 값 추출
        userService.deleteUser(id);
        return "redirect:/users"; //목록 페이지로 리다이렉트
    }
}