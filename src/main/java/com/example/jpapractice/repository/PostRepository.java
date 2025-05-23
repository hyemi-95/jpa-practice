package com.example.jpapractice.repository;

import com.example.jpapractice.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
    //기본적인 CURD는 JpaRepository에서 자동 제공
    //필요 시 findByTitleContaining(String keyword)같은 메서드 추가 가능
}
