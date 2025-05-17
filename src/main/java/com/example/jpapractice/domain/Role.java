package com.example.jpapractice.domain;

/**
 *  사용자 권한 구분
 * 실무에서도 일반적으로 USER / ADMIN Enum으로 구분함
 *
 * enum : 열거형, 정해진 값 중에서 하나만 선택되도록 제한하는 타입
 *
 *Role을 Enum으로 만든 이유 :
 * 정해진 값만 허용 -> "USER", "ADMIN" 외 다른 건 못 넣음 (안전성 ↑)
 * 오타 방지 -> 실수로 "Admin", "adminn", "user" 같은 걸 못 넣게 막아줌
 * 자동완성 가능 -> IDE에서 추천해줘서 편함
 * 가독성 향상 -> Role이라는 의미가 명확함 → 코드 읽기 쉬움
 *
 */
public enum Role{
    USER, ADMIN
}