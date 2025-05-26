package com.example.jpapractice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
    
    private Long id; //수정 시 필요
    
    @NotBlank(message = "제목은 필수입니다.")
    private String title;    
    
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
}
