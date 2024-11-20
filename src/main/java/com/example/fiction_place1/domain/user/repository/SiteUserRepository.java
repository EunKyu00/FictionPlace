package com.example.fiction_place1.domain.user.repository;

import com.example.fiction_place1.domain.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteUserRepository extends JpaRepository<SiteUser,Long> {
    boolean existsByUsername(String username); // username 중복 확인
    boolean existsByEmail(String email);       // email 중복 확인
}
