package com.example.jpapractice.security;

import com.example.jpapractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService  implements UserDetailsService {//클래스 역할: 로그인 시 이메일로 DB에서 사용자 조회, 없으면 예외발생

    private final UserRepository userRepository;

    //Spring Security가 로그인 시 자동으로 호출
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // email 파라미터는 로그인 화면에서 입력한 아이디 값
        // Optional + 람다식 => Optional인 이유는 UserRepository클래스에 그렇게 정의되어있기 때문에
        return userRepository.findByEmail(email)
                .map(CustomUserDetails::new)
                //Optional의 기능 중 하나로, 값이 있을 경우에만 동작. User가 존재하면 new CustomuserDetails(user)
                .orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다." + email)); // 비어있으면
    }
}
