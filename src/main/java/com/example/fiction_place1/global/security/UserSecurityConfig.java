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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class UserSecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and() // 기본 CORS 설정 추가
                .csrf().disable() // CSRF 비활성화 (API 통신을 위해 필요할 수 있음)
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()) // 모든 요청에 대해 허용
                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login") // 로그인 페이지 설정
                        .loginProcessingUrl("/user/login") // 로그인 처리 경로
                        .defaultSuccessUrl("/") // 로그인 성공 후 기본 페이지로 리다이렉트
                        .failureUrl("/user/login?error=true")) // 로그인 실패 시 다시 로그인 페이지로 리다이렉트
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

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000"); // React 개발 서버만 허용
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
