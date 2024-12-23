package com.example.fiction_place1.domain.message.repository;

import com.example.fiction_place1.domain.message.entity.Message;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    //보낸 쪽지
    Page<Message> findBySenderSiteUserOrderByCreatedDateDesc(SiteUser senderSiteUser, Pageable pageable);
    Page<Message> findBySenderCompanyUserOrderByCreatedDateDesc(CompanyUser senderCompanyUser, Pageable pageable);

    //받은 쪽지
    Page<Message> findByReceiverSiteUserOrderByCreatedDateDesc(SiteUser receiverSiteUser, Pageable pageable);
    Page<Message> findByReceiverCompanyUserOrderByCreatedDateDesc(CompanyUser receiverCompanyUser, Pageable pageable);

    //미확인 쪽지 수
    long countByReceiverSiteUserAndIsReadFalse(SiteUser receiverSiteUser);
    long countByReceiverCompanyUserAndIsReadFalse(CompanyUser receiverCompanyUser);
}
