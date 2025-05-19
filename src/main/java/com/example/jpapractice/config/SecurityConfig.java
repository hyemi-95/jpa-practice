package com.example.jpapractice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //설정 클래스임을 명시하는 어노테이션, Spring이 이 클래스를 읽고, 내부에 정의된 @Bean들을 등록
public class SecurityConfig {//
    //Spring Security 보안 규칙도 여기에 추가

    @Bean
    //이 메서드가 반환하는 객체를 Spring Bean으로 등록
    // passwordEncoder() 메서드가 반환하는 BCryptPasswordEncoder 객체는 스프링 전역에서 @Autowired 또는 생성자 주입으로 사용 가능,
    public PasswordEncoder passwordEncoder() { //Spring Security가 사용하는 비밀번호 암호화 객체를 반환하는 메서드
        return  new BCryptPasswordEncoder(); // 로그인 시 입력한 값과 해시값을 비교

        //PasswordEncoder는 인터페이스이고, 실제 구현체는 BCryptPasswordEncoder.
    }

    //로그인 인증 처리 매니처
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    //보안 필터 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf->csrf.disable()) // CSRF (사이트 간 요청 위조) 방지 기능을 끔 -> 테스트환경에서는 꺼도 되지만 실운영에서는 반트시 활성화해야함 ,
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/users").permitAll()//회원가입 저장 허용
                        .requestMatchers(HttpMethod.GET,"/users").hasRole("ADMIN")//목록은 관리자만
                        .requestMatchers("/","/login","/users/new","/users", "/css/**", "/js/++","/inages/**","/error")//여기에 있는 경로는 로그인 없이도 접근 가능
                        .permitAll()//위 경로들은 누구나 접근 가능
                        .anyRequest().authenticated()//나머지 모든 경로는 로그인해야 접근 가능
                )
                .formLogin(form -> form
                        .loginPage("/login")//커스텀 로그인 페이지(우리가 만든 로그인 페이지를 사용하겠다는 뜻), 인증이 안된 사용자라면 /login 페이지로 보내도록 하는 설정
                        .defaultSuccessUrl("/main",true)//로그인 성공 시 이동할 주소
                        .failureUrl("/login?error=true")//실패 시 에러 파라미터 추가
                        .permitAll()//로그인 페이지는 로그인 없이 접근 가능
                )
                .logout(logout-> logout
                        .logoutSuccessUrl("/login?logout") //  logout으로 로그아웃 요청하면 자동 처리됨, 로그아웃 성공 시 /login?logout 으로 이동
                        .permitAll()
                );

        return http.build(); //위에 설정한 모든 보안 규칙을 모아서 Spring Security가 실제로 동작할 수 있도록 SecurityFilterChain을 완성하는 것
    }

}

/***
 * [입력: 이메일 + 비밀번호]
 *    ↓
 * Spring Security → CustomUserDetailsService.loadUserByUsername()
 *    ↓
 * DB에서 사용자 조회 → 없으면 로그인 실패
 *    ↓
 * 조회된 User → CustomUserDetails 로 감싸서 리턴
 *    ↓
 * Spring Security 내부에서 비밀번호 비교 후 로그인 성공 or 실패*/

/**
 * authorizeHttpRequests() : URL별 접근 권한 설정
 * .permitAll() : 누구나 접근 허용(회원가입, 로그인 페이지 등)
 * .anyRequest().authenticated() : 나머지는 모두 로그인한 사용자만 접근 가능
 * .formLogin() :로그인 페이지와 성공 후 이동 경로 지정
 * .logout() : 로그아웃 처리 URL 지정
 * **/
