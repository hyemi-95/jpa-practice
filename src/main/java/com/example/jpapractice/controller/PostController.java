package com.example.jpapractice.controller;


import com.example.jpapractice.domain.Post;
import com.example.jpapractice.domain.User;
import com.example.jpapractice.security.CustomUserDetails;
import com.example.jpapractice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    //게시글 등록 폼
    @GetMapping("/new")
    public String showPostForm(Model model){
        model.addAttribute("post", Post.builder().build());
        return "post/postForm";
    }

    //게시글 등록
    @PostMapping("/new")
    public String createPost(@ModelAttribute Post post, @AuthenticationPrincipal CustomUserDetails userDetails){
        User writer = userDetails.getUser(); //게시글 저장 시 User객체 전체가 필요함 . 진짜 연관관계에 쓸 엔티티 객체를 그대로 주는 역할
        postService.savePost(post.getTitle(),post.getContent(), writer);
        return "redirect:/posts";
    }
    
    //게시글 목록
    @GetMapping
    public String listPosts(Model model){
        List<Post> posts = postService.findAllPost();
        model.addAttribute("posts",posts);
        return "post/postList";
    }

    //게시글 상세보기
    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model){
        Post post = postService.findPostById(id);
        //본인여부 판별
        boolean isOwner = userDetails != null && post.getWriter().getId().equals(userDetails.getUser().getId());

        model.addAttribute("post",post);
        model.addAttribute("isOwner",isOwner);
        return "post/postDetail";//수정화면
    }

    //수정 진입용
    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model){
        Post post = postService.findPostById(id);

        //작성자 검증
        if(!post.getWriter().getId().equals(userDetails.getUser().getId())){
            return "redirect:/posts/"+id+"?error=not_owner";
        }
        model.addAttribute("post",post);
        model.addAttribute("editMode",true); //수정모드 여부
        return "post/postForm";//수정화면
    }

    //수정 처리용
    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post postForm, @AuthenticationPrincipal CustomUserDetails userDetails){
        Post post = postService.findPostById(id);
        
        //작성자 검증
        if(!post.getWriter().getId().equals(userDetails.getUser().getId())){
            return "redirect:/posts/"+id+"?error=not_owner";
        }
        postService.updatePost(id, postForm.getTitle(), postForm.getContent());
        return "redirect:/posts/"+id;//수정화면
    }
}
