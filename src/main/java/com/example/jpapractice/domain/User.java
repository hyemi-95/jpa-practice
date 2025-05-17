package  com.example.jpapractice.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter //모든 필드에 대한 Getter 자동 생성
@NoArgsConstructor(access =  AccessLevel.PROTECTED) // JPA 기본 생성자(외부 생성 막음)
@AllArgsConstructor //모든 필드를 매개로 한 생성자 생성
@Builder // 이 생성자 기반으로 builder 생성됨!
public class User {
    
    @Id //기본 키(PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB에서 자동 증가 ID 사용(시퀀스)
    private Long id;

    @Column(nullable = false, length = 50) //이름은 필수
    private String name;
    
    @Column(nullable = false, unique = true,length = 100) //이메일은 유일 + 필수
    private String email;
    
    @Enumerated(EnumType.STRING) // USER / ADMIN  으로 저장
    @Column(nullable = false) // 필수
    private Role role;
}

/* Entity:
 * 이 클래스는 JPA가 관리하는 DB테이블 매핑 대상
 * 이걸 선언해야 Hibernate(JPA 구현체)가 이 클래스를 테이블처럼 인식함
 * 클래스명이 User면, 기본적으로 테이블명은 user로 생성됨 -> 원하면 @Table(name = "users")로 바꿀 수도 있음
 * JPA는 @Id가 없는 클래스는 Entity로 처리하지 않음 → 오류남
 *
 * Builder :  .builder() 형태로 객체 생성 가능 → 가독성 + 유연성 최고
 *
 * Id : 이 필드가 테이블의 기본 키(PK)라는 뜻
 *
 * GeneratedValue :  DB가 자동으로 ID 값을 생성하도록 설정 , IDENTITY는 DB에 위임하는 전략
 *
 * Column : DB의 컬럼 속성 지정 -> 지정하지 않으면 기본값으로 nullable = true, length = 255
 *
 * Enumerated : Role.java에서 Role을 Enum으로 만들었기 때문에 @Enumerated(EnumType.STRING) 붙여서 DB에 "USER" / "ADMIN"으로 안전하게 저장 가능
 * Enum : DB에 어떻게 저장할지 결정
 *
 */

