package com.example.jpapractice.service;

import com.example.jpapractice.domain.Post;
import com.example.jpapractice.domain.User;
import com.example.jpapractice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
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
        postRepository.deleteById(id);
    }

    //게시글 수정
    @Transactional
    public void updatePost(Long id, String title, String content){
        Post post = findPostById(id);
        post.setTitle(title);
        post.setContent(content);
    }
}
