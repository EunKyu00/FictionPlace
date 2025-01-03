package com.example.fiction_place1.domain.user.service;

import com.example.fiction_place1.domain.board.entity.Board;
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

    public void modifySiteUser(SiteUser siteUser, String nickname, String email, String password) {
        // 이메일이나 닉네임 중복 체크 (비어 있지 않은 경우에만)
        if (nickname != null && !nickname.isEmpty() && !nickname.equals(siteUser.getNickname()) && siteUserRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        if (email != null && !email.isEmpty() && !email.equals(siteUser.getEmail()) && siteUserRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 닉네임이 입력되었으면 변경 (비어 있지 않은 경우에만)
        if (nickname != null && !nickname.isEmpty()) {
            siteUser.setNickname(nickname);
        }

        // 이메일이 입력되었으면 변경 (비어 있지 않은 경우에만)
        if (email != null && !email.isEmpty()) {
            siteUser.setEmail(email);
        }

        // 비밀번호가 비어 있지 않으면 변경
        if (password != null && !password.isEmpty()) {
            siteUser.setPassword(passwordEncoder.encode(password));
        }

        // 변경된 사용자 정보 저장
        this.siteUserRepository.save(siteUser);
    }


    // findById 메서드 추가
    public SiteUser findById(Long id) {
        return siteUserRepository.findById(id).orElse(null);  // Optional을 null로 처리
    }
}
