package com.example.fiction_place1.domain.user.service;

import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.form.CompanyUserCreateForm;
import com.example.fiction_place1.domain.user.repository.CompanyUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
