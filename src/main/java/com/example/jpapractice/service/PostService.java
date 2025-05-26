package com.example.jpapractice.service;

import com.example.jpapractice.domain.Post;
import com.example.jpapractice.domain.User;
import com.example.jpapractice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    //게시글 등록
    @Transactional
    public Post savePost(String title, String content, User writer){

        Post post = Post.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .build();

        return postRepository.save(post);

    }

    //전체 게시글 조회
    public List<Post> findAllPost(){
        return  postRepository.findAll();
    }

    //게시글 단건 조회
    public Post findPostById(Long id){
        return  postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다. ID = "+id));
    }

    //게시글 삭제
    @Transactional
    public void deletePost(Long id){
        Post post = findPostById(id);
        postRepository.delete(post);
    }

    //게시글 수정
    @Transactional
    public void updatePost(Long id, String title, String content){
        Post post = findPostById(id);
        post.setTitle(title);
        post.setContent(content);
    }

    /***
     * Page<T> 는 Spring Data JPA에서 제공하는 페이징 결과 객체
     * 페이징에서 필요한 총 페이지 수, 현재 페이지, 다음/이전 페이지 존재 여부 등의 정보 포함
     * **/
    public Page<Post> getPostList(int page) { 
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());//createdAt 필드 기준으로 내림차순
        return postRepository.findAll(pageable); // JpaRepository는 findAll(Pageable pageable) 기본제공ㄹ -> 페이징 정보 넘기면 알아서 해당 페이지 데이터만 추출해줌
    }
}
