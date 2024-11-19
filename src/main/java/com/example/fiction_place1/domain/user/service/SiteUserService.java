package com.example.fiction_place1.domain.user.service;

import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.form.SiteUserCreateForm;
import com.example.fiction_place1.domain.user.repository.SiteUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void siteUser(SiteUserCreateForm siteUserCreateForm){
        String encodedPassword = passwordEncoder.encode(siteUserCreateForm.getPassword());

        SiteUser siteUser = new SiteUser();
        siteUser.setUsername(siteUserCreateForm.getUsername());
        siteUser.setPassword(encodedPassword);
        siteUser.setEmail(siteUserCreateForm.getEmail());
        siteUser.setNickname(siteUserCreateForm.getNickname());

        siteUserRepository.save(siteUser);

    }
}
