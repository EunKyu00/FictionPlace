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
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableWebSecurity
public class UserSecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 권한 설정
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers("/**", "/CSS/**", "/JS/**", "/login/**", "/signup/**", "/images/**").permitAll() // 로그인 페이지 및 정적 리소스 허용
                        .requestMatchers("/user/**").hasRole("USER") // 일반 회원 전용 경로
                        .requestMatchers("/company/**").hasRole("COMPANY") // 기업 회원 전용 경로
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자 경로 제한
                        .anyRequest().authenticated()) // 그 외 요청은 인증 필요

                // 일반 회원 로그인 설정
                .formLogin((formLogin) -> formLogin
                        .loginPage("/login/user") // 일반 회원 로그인 페이지
                        .loginProcessingUrl("/login/user") // 로그인 처리 경로
                        .defaultSuccessUrl("/") // 로그인 성공 후 이동할 경로
                        .failureUrl("/login/user?error=true")) // 로그인 실패 시 이동 경로

                // 기업 회원 로그인 설정
                .formLogin((formLogin) -> formLogin
                        .loginPage("/login/company") // 기업 회원 로그인 페이지
                        .loginProcessingUrl("/login/company") // 로그인 처리 경로
                        .defaultSuccessUrl("/") // 로그인 성공 후 이동할 경로
                        .failureUrl("/login/company?error=true")) // 로그인 실패 시 이동 경로
                //로그아웃 설정
                .logout((logout) -> logout
                        .logoutRequestMatcher(new OrRequestMatcher(
                                new AntPathRequestMatcher("/user/logout"), // 일반회원 로그아웃 경로
                                new AntPathRequestMatcher("/company/logout") // 기업회원 로그아웃 경로
                        )) // 두 경로 모두 로그아웃 처리
                        .logoutSuccessUrl("/") // 로그아웃 후 리다이렉트 경로
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .addLogoutHandler((request, response, authentication) -> {
                            // 로그아웃 처리가 제대로 되었는지 확인
                            String logoutPath = request.getRequestURI(); // 요청된 로그아웃 경로 확인
                            if (logoutPath.equals("/user/logout")) {
                                // 일반회원 로그아웃 처리
                                System.out.println("일반회원 로그아웃 처리");
                            } else if (logoutPath.equals("/company/logout")) {
                                // 기업회원 로그아웃 처리
                                System.out.println("기업회원 로그아웃 처리");
                            }
                        }));


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