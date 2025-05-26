package com.example.jpapractice.controller;


import com.example.jpapractice.domain.Post;
import com.example.jpapractice.domain.User;
import com.example.jpapractice.dto.PostDto;
import com.example.jpapractice.security.CustomUserDetails;
import com.example.jpapractice.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @PostMapping("/new") //25.05.24 유효성검사 추가
    public String createPost(@Valid @ModelAttribute("post") PostDto postDto, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails userDetails, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("post",postDto);
            return "post/postForm";
        }
        User writer = userDetails.getUser(); //게시글 저장 시 User객체 전체가 필요함 . 진짜 연관관계에 쓸 엔티티 객체를 그대로 주는 역할
        postService.savePost(postDto.getTitle(),postDto.getContent(), writer);
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

        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());

        model.addAttribute("post",postDto);
        model.addAttribute("editMode",true); //수정모드 여부
        return "post/postForm";//수정화면
    }

    //수정 처리용
    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable Long id,@Valid @ModelAttribute("post") PostDto postDto, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails userDetails, Model model){
        Post post = postService.findPostById(id);
        //작성자 검증
        if(!post.getWriter().getId().equals(userDetails.getUser().getId())){
            return "redirect:/posts/"+id+"?error=not_owner";
        }
        if(bindingResult.hasErrors()){
            model.addAttribute("post",postDto);
            model.addAttribute("editMode",true); //수정모드 여부
            return "post/postForm";
        }
        postService.updatePost(id, postDto.getTitle(), postDto.getContent());
        return "redirect:/posts/"+id;//게시글 상세보기
    }

    //삭제처리
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, RedirectAttributes redirectAttributes){
        Post post = postService.findPostById(id);

        //작성자 검증
        if(!post.getWriter().getId().equals(userDetails.getUser().getId())){
            redirectAttributes.addFlashAttribute("errorMessage","작성자만 삭제할 수 있습니다.");//리다이렉트 이후 1회만 유지되는 메시지 전달 가능, Model과 다르게 URL에 파라미터가 안 붙음
            return "redirect:/posts/"+id;
        }
        postService.deletePost(id);
        redirectAttributes.addFlashAttribute("successMessage","게시글이 성공적으로 삭제되었습니다.");
        return "redirect:/posts";//삭제 후 목록으로 이동
    }
}
