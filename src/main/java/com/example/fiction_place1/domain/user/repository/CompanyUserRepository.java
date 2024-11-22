package com.example.fiction_place1.domain.user.repository;

import com.example.fiction_place1.domain.user.entity.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyUserRepository extends JpaRepository<CompanyUser,Long> {
    boolean existsByCompanyName(String companyName);
    boolean existsByEmail(String email);
    Optional<CompanyUser> findByCompanyName(String companyName);
}
