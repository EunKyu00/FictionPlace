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
    // findById 메서드 추가
    public CompanyUser findById(Long id) {
        return companyUserRepository.findById(id).orElse(null);  // Optional을 null로 처리
    }

    public void modifyCompanyUser(CompanyUser companyUser, String companyName, String email, String password){
        if (companyName != null && !companyName.isEmpty() && !companyName.equals(companyUser.getCompanyName()) && companyUserRepository.existsByCompanyName(companyName)){
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
        if (email != null && !email.isEmpty() && !email.equals(companyUser.getEmail()) && companyUserRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 닉네임이 입력되었으면 변경 (비어 있지 않은 경우에만)
        if (companyName != null && !companyName.isEmpty()) {
            companyUser.setCompanyName(companyName);
        }

        // 이메일이 입력되었으면 변경 (비어 있지 않은 경우에만)
        if (email != null && !email.isEmpty()) {
            companyUser.setEmail(email);
        }

        // 비밀번호가 비어 있지 않으면 변경
        if (password != null && !password.isEmpty()) {
            companyUser.setPassword(passwordEncoder.encode(password));
        }

        // 변경된 사용자 정보 저장
        this.companyUserRepository.save(companyUser);
    }
}
