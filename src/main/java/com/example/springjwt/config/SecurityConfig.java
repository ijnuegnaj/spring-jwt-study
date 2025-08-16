package com.example.springjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 활성화
public class SecurityConfig {

    // 비밀번호 암호화를 위한 Bean 등록
    // 회원가입 시 사용자의 비밀번호를 해시 암호화할 때 필요
    // Spring Security는 기본적으로 "평문 비밀번호"를 허용하지 않기 때문에 반드시 암호화 필요
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CSRF(Cross-Site Request Forgery) 보안 기능 끄기
        // REST API에서는 보통 CSRF 토큰을 쓰지 않고 JWT로 인증을 하기 때문에 disable
        http
                .csrf((auth) -> auth.disable());

        // Form 로그인 방식 사용 안 함
        // 스프링 시큐리티에서 기본 제공하는 로그인 화면을 비활성화
        http
                .formLogin((auth) -> auth.disable());

        // HTTP Basic 인증 방식 사용 안 함
        // 브라우저 팝업으로 뜨는 기본 인증 창을 비활성화
        http
                .httpBasic((auth) -> auth.disable());

        // 요청 경로별 접근 권한 설정
        http
                .authorizeHttpRequests((auth) -> auth
                        // 로그인, 회원가입, 메인 페이지는 누구나 접근 가능
                        .requestMatchers("/login", "/", "/join").permitAll()
                        // "/admin" 경로는 "ADMIN" 권한이 있는 사용자만 접근 가능
                        .requestMatchers("/admin").hasRole("ADMIN")
                        // 그 외 모든 요청은 인증된 사용자만 접근 가능
                        .anyRequest().authenticated());

        // 세션 관리 정책 설정
        http
                .sessionManagement((session) -> session
                        // 세션을 사용하지 않고, STATELESS 모드로 설정
                        // → 매 요청마다 JWT 같은 토큰을 검사해서 인증
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                        //stateless 가장 중요
        // 최종적으로 SecurityFilterChain 객체를 반환
        return http.build();
    }
}
