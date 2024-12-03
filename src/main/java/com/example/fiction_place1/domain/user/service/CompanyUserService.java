package com.example.fiction_place1.domain.user.service;

import com.example.fiction_place1.domain.profile.entity.MyProfile;
import com.example.fiction_place1.domain.profile.service.MyProfileService;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.form.CompanyUserCreateForm;
import com.example.fiction_place1.domain.user.repository.CompanyUserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyUserService {
    private final CompanyUserRepository companyUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final MyProfileService myProfileService;
    @Transactional
    public void companyUser(CompanyUserCreateForm companyUserCreateForm){
        String encodedPassword = passwordEncoder.encode(companyUserCreateForm.getPassword());

        CompanyUser companyUser = new CompanyUser();
        companyUser.setCompanyName(companyUserCreateForm.getCompanyName());
        companyUser.setBusinessRegistrationNumber(companyUserCreateForm.getBusinessRegistrationNumber());
        companyUser.setEmail(companyUserCreateForm.getEmail());
        companyUser.setContactPerson(companyUserCreateForm.getContactPerson());
        companyUser.setPassword(encodedPassword);
        companyUser.setRole("COMPANY");
        companyUserRepository.save(companyUser);

        // MyProfile 생성 및 저장
        MyProfile profile = new MyProfile();
        profile.setCompanyUser(companyUser);
        profile.setDescription("Default description"); // 기본값
        if (profile.getProfileImage() == null) {
            profile.setProfileImage("/images/unnamed.png"); // 기본 이미지 경로
        } // 기본값

        // 프로필 저장
        myProfileService.saveProfile(profile);

    }
    // 로그인 검증 메서드 추가
    public CompanyUser login(String companyName, String password) {
        Optional<CompanyUser> companyUserOptional = companyUserRepository.findByCompanyName(companyName); // 수정

        if (companyUserOptional.isEmpty()) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        CompanyUser companyUser = companyUserOptional.get();
        if (!passwordEncoder.matches(password, companyUser.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return companyUser;
    }

    // 현재 로그인한 회사 사용자 가져오기
    public CompanyUser getLoggedInCompanyUser(HttpSession session) {
        // 세션에서 회사 사용자 정보 가져오기
        CompanyUser loggedInCompanyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        // 로그인된 회사 사용자가 없는 경우 예외 발생
        if (loggedInCompanyUser == null) {
            throw new IllegalStateException("로그인된 회사 사용자가 없습니다.");
        }

        return loggedInCompanyUser;
    }
    // findById 메서드 추가
    public CompanyUser findById(Long id) {
        return companyUserRepository.findById(id).orElse(null);  // Optional을 null로 처리
    }
}
