package com.example.fiction_place1.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class UserSecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()) // 모든 요청에 대해 허용
                //일반 회원 로그인
                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login") // 일반 회원 로그인 페이지
                        .loginProcessingUrl("/user/login") // 로그인 처리 경로
                        .defaultSuccessUrl("/") // 로그인 성공 후 기본 페이지로 리다이렉트
                        .failureUrl("/user/login?error=true")) // 로그인 실패 시 다시 로그인 페이지로 리다이렉트

                .formLogin((formLogin) -> formLogin
                        .loginPage("/company/login")
                        .loginProcessingUrl("company/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/company/login?error=true"))

                // 로그아웃 설정
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // 일반회원 로그아웃 경로
                        .logoutRequestMatcher(new AntPathRequestMatcher("/company/logout"))
                        .logoutSuccessUrl("/") // 로그아웃 후 리다이렉트할 경로
                        .invalidateHttpSession(true)); // 세션 무효화


        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화
    }
}
