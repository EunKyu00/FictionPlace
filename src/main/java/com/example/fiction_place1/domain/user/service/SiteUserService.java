package com.example.fiction_place1.domain.user.service;

import com.example.fiction_place1.domain.profile.entity.MyProfile;
import com.example.fiction_place1.domain.profile.service.MyProfileService;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.form.SiteUserCreateForm;
import com.example.fiction_place1.domain.user.repository.SiteUserRepository;
import jakarta.servlet.http.HttpSession;
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
    private final MyProfileService myProfileService;

    @Transactional
    public void siteUser(SiteUserCreateForm siteUserCreateForm) {
        String encodedPassword = passwordEncoder.encode(siteUserCreateForm.getPassword());

        SiteUser siteUser = new SiteUser();
        siteUser.setUsername(siteUserCreateForm.getUsername());
        siteUser.setPassword(encodedPassword);
        siteUser.setEmail(siteUserCreateForm.getEmail());
        siteUser.setNickname(siteUserCreateForm.getNickname());
        siteUser.setRole("USER");


        // MyProfile 생성 및 저장
        MyProfile profile = new MyProfile();
        profile.setSiteUser(siteUser);
        profile.setDescription("Default description"); // 기본값
        siteUser.setMyProfile(profile);
        myProfileService.saveProfile(profile);

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

    // 현재 로그인한 사용자 가져오기
    public SiteUser getLoggedInUser(HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        SiteUser loggedInUser = (SiteUser) session.getAttribute("loginUser");

        // 로그인된 사용자가 없는 경우 예외 발생
        if (loggedInUser == null) {
            throw new IllegalStateException("로그인된 사용자가 없습니다.");
        }

        return loggedInUser;
    }

    //일반유저 수정 로직 (닉네임, 자기소개, 이메일)
    public void updateUser(SiteUser currentUser, SiteUser updatedUser) {
        currentUser.setNickname(updatedUser.getNickname());
        currentUser.setEmail(updatedUser.getEmail());
        if (!updatedUser.getPassword().isEmpty()) {
            currentUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (updatedUser.getMyProfile() != null && updatedUser.getMyProfile().getDescription() != null) {
            currentUser.getMyProfile().setDescription(updatedUser.getMyProfile().getDescription());
        }
        siteUserRepository.save(currentUser);
    }
    // findById 메서드 추가
    public SiteUser findById(Long id) {
        return siteUserRepository.findById(id).orElse(null);  // Optional을 null로 처리
    }
}
