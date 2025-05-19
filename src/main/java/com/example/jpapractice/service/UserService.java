package com.example.jpapractice.service;

import com.example.jpapractice.domain.Role;
import com.example.jpapractice.domain.User;
import com.example.jpapractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service // 스프링이 이 클래스를 서비스로 인식 (빈 등록)
@RequiredArgsConstructor // 생성자 주입 (final 필드만 자동 생성자 생성)
@Transactional(readOnly = true) //조회 전용 메서드는 읽기 최적화 설정
public class UserService {

    private final UserRepository userRepository; //의존성 주입 대상
    private final PasswordEncoder passwordEncoder;// 패스워드 암호화

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
    public User saveUser(String name, String email, String password, Role role) {
        if(userRepository.findByEmail(email).isPresent()){ // isPresent() -> 	값이 있는지 확인
            throw new IllegalStateException("이미 사용 중인 이메일입니다."); //중복이면 예외 발생시켜서 컨트롤러에서 처리하게 함
        }

        String encodePassword = passwordEncoder.encode(password);//패스워드 암호화

        User user = User.builder() //Lombok의 @Builder 패턴을 활용한 객체 생성 방식, UserBuilder라는 내부 클래스를 Lombok이 자동 생성
                .name(name) //생성자 파라미터는 순서가 다르면 오류가 발생하지만 ,빌더방식은 순서 상관없이 명시적,
                .email(email)
                .password(encodePassword)//암호화된 패스워드 저장
                .role(role)
                .build();//->지금까지 설정한 값으로 실제 User 객체를 생성함, 내부적으로 new User(...) 생성자 호출 됨

        return userRepository.save(user);
    }

    // 회원 삭제
    @Transactional //등록/삭제 등 변경 작업에는 별도 트랜잭션 선언
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    };

}