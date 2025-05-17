package com.example.jpapractice.repository;

import com.example.jpapractice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/***
 * JpaRepository : Spring Data JPA가 제공하는 인터페이스. 기본 CRUD (save, findAll, findById, deleteById) 자동 제공
 */

public interface UserRepository extends JpaRepository<User, Long> {
    // 추가적인 쿼리 메서드는 나중에 여기에 정의 가능함
}