package com.example.fiction_place1.domain.message.service;

import com.example.fiction_place1.domain.message.entity.Message;
import com.example.fiction_place1.domain.message.repository.MessageRepository;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.entity.User;
import com.example.fiction_place1.domain.user.repository.CompanyUserRepository;
import com.example.fiction_place1.domain.user.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final SiteUserRepository siteUserRepository;
    private final CompanyUserRepository companyUserRepository;

    public void sendMessage(String title, String content, User sender, User receiver) {
        Message message = new Message();
        message.setTitle(title);
        message.setContent(content);

        if (sender instanceof SiteUser) {
            SiteUser siteUser = (SiteUser) sender;
            message.setSenderSiteUser(siteUser);
        } else if (sender instanceof CompanyUser) {
            CompanyUser companyUser = (CompanyUser) sender;
            message.setSenderCompanyUser(companyUser);
        }

        if (receiver instanceof SiteUser) {
            SiteUser siteUser = (SiteUser) receiver;
            message.setReceiverSiteUser(siteUser);
        } else if (receiver instanceof CompanyUser) {
            CompanyUser companyUser = (CompanyUser) receiver;
            message.setReceiverCompanyUser(companyUser);
        }

        this.messageRepository.save(message);
    }

    public SiteUser findByNickname(String nickname) {
        return siteUserRepository.findByNickname(nickname)
                .orElse(null);
    }

    public CompanyUser findByCompanyName(String companyName){
        return companyUserRepository.findByCompanyName(companyName)
                .orElse(null);
    }

    public void markAsRead(Message message) {
        message.setRead(true);  // 읽음 처리
        messageRepository.save(message);  // 데이터베이스에 반영
    }
    public Message findById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("쪽지를 찾을 수 없습니다."));
    }
    public Page<Message> getReceiverSiteUserMessage(SiteUser siteUser, Pageable pageable) {
        return messageRepository.findByReceiverSiteUserOrderByCreatedDateDesc(siteUser, pageable);
    }

    public Page<Message> getReceiverCompanyUserMessage(CompanyUser companyUser, Pageable pageable) {
        return messageRepository.findByReceiverCompanyUserOrderByCreatedDateDesc(companyUser, pageable);
    }
    public Page<Message> getSenderSiteUserMessage(SiteUser siteUser,Pageable pageable){
        return messageRepository.findBySenderSiteUserOrderByCreatedDateDesc(siteUser,pageable);
    }

    public Page<Message> getSenderCompanyMessage(CompanyUser companyUser,Pageable pageable){
        return messageRepository.findBySenderCompanyUserOrderByCreatedDateDesc(companyUser,pageable);
    }
    public long countUnreadMessagesForUser(SiteUser siteUser) {
        return messageRepository.countByReceiverSiteUserAndIsReadFalse(siteUser);
    }

    // CompanyUser의 미확인 쪽지 수
    public long countUnreadMessagesForCompanyUser(CompanyUser companyUser) {
        return messageRepository.countByReceiverCompanyUserAndIsReadFalse(companyUser);
    }
}

