package com.example.fiction_place1.domain.user.service;

import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.form.SiteUserCreateForm;
import com.example.fiction_place1.domain.user.repository.SiteUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void siteUser(SiteUserCreateForm siteUserCreateForm) {
        String encodedPassword = passwordEncoder.encode(siteUserCreateForm.getPassword());

        SiteUser siteUser = new SiteUser();
        siteUser.setUsername(siteUserCreateForm.getUsername());
        siteUser.setPassword(encodedPassword);
        siteUser.setEmail(siteUserCreateForm.getEmail());
        siteUser.setNickname(siteUserCreateForm.getNickname());

        siteUserRepository.save(siteUser);

    }

    // 로그인 검증 메서드 추가
    public SiteUser login(String username, String password) {
        Optional<SiteUser> userOptional = siteUserRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        SiteUser user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return user; // 로그인 성공 시 사용자 정보 반환
    }
}
