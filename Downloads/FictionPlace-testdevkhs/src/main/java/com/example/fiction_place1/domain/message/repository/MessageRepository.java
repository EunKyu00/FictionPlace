package com.example.fiction_place1.domain.message.repository;

import com.example.fiction_place1.domain.message.entity.Message;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderSiteUser(SiteUser senderSiteUser);
    List<Message> findBySenderCompanyUser(CompanyUser senderCompanyUser);

    List<Message> findByReceiverSiteUser(SiteUser receiverSiteUser);
    List<Message> findByReceiverCompanyUser(CompanyUser receiverCompanyUser);
}
