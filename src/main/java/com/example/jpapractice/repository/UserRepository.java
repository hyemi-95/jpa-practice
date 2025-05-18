package com.example.jpapractice.repository;

import com.example.jpapractice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/***
 * JpaRepository : Spring Data JPA가 제공하는 인터페이스. 기본 CRUD (save, findAll, findById, deleteById) 자동 제공
 */

public interface UserRepository extends JpaRepository<User, Long> {
    // 추가적인 쿼리 메서드는 나중에 여기에 정의 가능함
    Optional<User> findByEmail(String email);//이메일로 사용자 검색(중복검사를 위해)
    //Optional :사용자가 있거나 없을 수 있어서 null이 올 수 있는 값을 감싸는 래퍼 클래스 -> 즉, 값이 있으면 감싸고, 없으면 빈(empty) 상태로 표현할 수 있는 객체
    //findByEmail(String email) ->  SELECT * FROM user WHERE email = ? 로 jpa가 자동구현
}