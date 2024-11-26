package com.example.fiction_place1.domain.user.service;

import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.form.CompanyUserCreateForm;
import com.example.fiction_place1.domain.user.repository.CompanyUserRepository;
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

    @Transactional
    public void companyUser(CompanyUserCreateForm companyUserCreateForm){
        String encodedPassword = passwordEncoder.encode(companyUserCreateForm.getPassword());

        CompanyUser companyUser = new CompanyUser();
        companyUser.setCompanyName(companyUserCreateForm.getCompanyName());
        companyUser.setBusinessRegistrationNumber(companyUserCreateForm.getBusinessRegistrationNumber());
        companyUser.setEmail(companyUserCreateForm.getEmail());
        companyUser.setContactPerson(companyUserCreateForm.getContactPerson());
        companyUser.setPassword(encodedPassword);

        companyUserRepository.save(companyUser);

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

}
