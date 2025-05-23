package com.example.jpapractice.security;

import com.example.jpapractice.domain.Role;
import com.example.jpapractice.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor // final로 선언된 필드를 자동 생성자로 주입해주는 Lombok 어노테이션
public class CustomUserDetails implements UserDetails {
    //클래스의 역할 : User 도메인을 Spring Security에서 사용할 수 있게 감싸는 어댑터 역할
    //로그인된 유저의 이메일, 이름, 권한 등 모든 정보를 꺼낼 수 있게 해주는 어댑터 클래스
    
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+ user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();//암호화된 비밀번호
    }

    @Override
    public String getUsername() {
        return user.getEmail();// 로그인 ID기준은 email
    }

    // 계정 만료, 잠김, 인증 유효성 설정 (기본 true)
    //->계정이 만료되지 않았고, 잠기지 않았고, 비밀번호도 유효하고, 사용 가능한 계정이다.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getName() {//사용자 이름 출력용
        return user.getName();
    }

    public Role getRole() {//권한 필요 시 사용
        return user.getRole();
    }

    public User getUser() {
        return user;
    }
}
