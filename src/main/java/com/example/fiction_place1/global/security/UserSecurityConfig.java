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
                // CSRF 예외 설정
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/profile/user/**") // CSRF 검증 제외
                )
                // 권한 설정
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers("/**", "/CSS/**", "/JS/**", "/login/**", "/signup/**", "/images/**").permitAll() // 로그인 페이지 및 정적 리소스 허용
                        .requestMatchers("/user/**").hasRole("USER") // 일반 회원 전용 경로
                        .requestMatchers("/company/**").hasRole("COMPANY") // 기업 회원 전용 경로
                        .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자 경로 제한
                        .requestMatchers("/profile/user/**").authenticated() // 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()) // 그 외 요청은 인증 필요
                // 단일 로그인 설정
                .formLogin((formLogin) -> formLogin
                        .loginPage("/login") // 공통 로그인 페이지
                        .loginProcessingUrl("/perform_login") // 공통 로그인 처리 경로
                        .successHandler((request, response, authentication) -> {
                            String username = authentication.getName();
                            if (request.getRequestURI().equals("/login/user")) {
                                response.sendRedirect("/");
                            } else if (request.getRequestURI().equals("/")) {
                                response.sendRedirect("/company/home");
                            } else {
                                response.sendRedirect("/");
                            }
                        })
                        .failureUrl("/login?error=true")) // 로그인 실패 시 공통 실패 페이지

                // 로그아웃 설정
                .logout((logout) -> logout
                        .logoutRequestMatcher(new OrRequestMatcher(
                                new AntPathRequestMatcher("/user/logout"), // 일반회원 로그아웃 경로
                                new AntPathRequestMatcher("/company/logout") // 기업회원 로그아웃 경로
                        ))
                        .logoutSuccessUrl("/") // 로그아웃 성공 후 리다이렉트 경로
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 삭제
                        .addLogoutHandler((request, response, authentication) -> {
                            // 로그아웃 처리 확인
                            String logoutPath = request.getRequestURI();
                            if (logoutPath.equals("/user/logout")) {
                            } else if (logoutPath.equals("/company/logout")){
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
