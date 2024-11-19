package com.example.fiction_place1.domain.user.repository;

import com.example.fiction_place1.domain.user.entity.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyUserRepository extends JpaRepository<CompanyUser,Long> {
    boolean existsByCompanyName(String companyName);  // 올바른 메서드 이름
    boolean existsByEmail(String email);
}
