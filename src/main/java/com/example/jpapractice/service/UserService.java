package com.example.jpapractice.service;

import com.example.jpapractice.domain.Role;
import com.example.jpapractice.domain.User;
import com.example.jpapractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service // 스프링이 이 클래스를 서비스로 인식 (빈 등록)
@RequiredArgsConstructor // 생성자 주입 (final 필드만 자동 생성자 생성)
@Transactional(readOnly = true) //조회 전용 메서드는 읽기 최적화 설정
public class UserService {

    private final UserRepository userRepository; //의존성 주입 대상

    // 회원 전체 조회
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    // 단일 회원 조회 -> findById()는 null이 아닌 Optional로 안전하게 반환
    public Optional<User> findUserById(Long id){
        return userRepository.findById(id);
    }

    //회원 등록(입력이므로 별도로 Transactional어노테이션 필요)
    @Transactional
    public User saveUser(String name, String email, Role role) {
        User user = User.builder()
                .name(name)
                .email(email)
                .role(role)
                .build();

        return userRepository.save(user);
    }

    // 회원 삭제
    @Transactional //등록/삭제 등 변경 작업에는 별도 트랜잭션 선언
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

}