package com.example.fiction_place1.domain.message.controller;

import com.example.fiction_place1.domain.message.entity.Message;
import com.example.fiction_place1.domain.message.form.MessageForm;
import com.example.fiction_place1.domain.message.service.MessageService;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.entity.User;
import com.example.fiction_place1.domain.user.service.SiteUserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final SiteUserService siteUserService;

    //받은 쪽지 보관함
    @GetMapping("/message/menu")
    public String messageMenu(Model model, HttpSession session,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size) {

        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        if (siteUser == null && companyUser == null){
            return "access_denied";
        }

        Pageable pageable = PageRequest.of(page, size);

        if (siteUser != null) {
            Page<Message> messagePage = messageService.getReceiverSiteUserMessage(siteUser,pageable);
            model.addAttribute("messagePage",messagePage);
            // 페이지 내의 메시지 목록을 가져와 읽지 않은 메시지 수를 계산
            long unreadCountForSiteUser = messageService.countUnreadMessagesForUser(siteUser);
            model.addAttribute("unreadCountForSiteUser", unreadCountForSiteUser);
        }
        if (companyUser != null){
            Page<Message> messagePage = messageService.getReceiverCompanyUserMessage(companyUser,pageable);
            model.addAttribute("messagePage",messagePage);

            long unreadCountForCompanyUser = messageService.countUnreadMessagesForCompanyUser(companyUser);
            model.addAttribute("unreadCountForCompanyUser", unreadCountForCompanyUser);
        }

        return "message_menu";
    }

    //보낸 쪽지
    @GetMapping("/message/sent")
    public String sentMessages(Model model, HttpSession session,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size) {
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        if (siteUser == null && companyUser == null) {
            return "access_denied";
        }

        Pageable pageable = PageRequest.of(page, size);

        if (siteUser != null) {
            Page<Message> messages = messageService.getSenderSiteUserMessage(siteUser,pageable);
            model.addAttribute("messagePage", messages);
        }

        if (companyUser != null) {
            Page<Message> messages = messageService.getSenderCompanyMessage(companyUser,pageable);
            model.addAttribute("messagePage", messages);
        }

        return "message_sent";  // fallback, 일반적으로는 이 부분까지 오지 않음
    }


    @GetMapping("/message/send")
    public String sendMessage(Model model,HttpSession session){
        model.addAttribute("messageForm", new MessageForm());
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        if (siteUser == null && companyUser == null){
            return "access_denied";
        }
        return "message_send";
    }

    //쪽지 보내기
    @PostMapping("/message/send")
    public String sendMessage(
            @ModelAttribute("messageForm") @Valid MessageForm messageForm,
            BindingResult bindingResult,
            HttpSession session,
            Model model) {

        // 로그인된 사용자 세션 받아오기
        SiteUser sessionSiteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser sessionCompanyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        model.addAttribute("messageForm", messageForm);

        if (bindingResult.hasErrors()) {
            // 입력값 오류가 있을 경우, 다시 폼으로 돌아감
            return "message_send";
        }

        User sender = null; // 발신자
        if (sessionSiteUser != null) {
            sender = sessionSiteUser; // 일반 사용자로 로그인한 경우
        } else if (sessionCompanyUser != null) {
            sender = sessionCompanyUser; // 기업 사용자로 로그인한 경우
        }

        if (sender == null) {
            // 로그인 정보가 없는 경우 처리
            bindingResult.reject("unauthorized", "로그인이 필요합니다.");
            return "message_send";
        }

        // 수신자가 입력되었는지 확인
        if (messageForm.getReceiver() == null || messageForm.getReceiver().isEmpty()) {
            bindingResult.rejectValue("receiver", "empty", "수신자를 입력해주세요.");
            return "message_send";  // 수신자가 없으면 폼을 다시 보여줍니다.
        }

// 닉네임 또는 기업명으로 수신자 찾기
        User receiver = messageService.findByNickname(messageForm.getReceiver());
        if (receiver == null) {
            receiver = messageService.findByCompanyName(messageForm.getReceiver());
        }

// 수신자가 존재하는지 확인
        if (receiver == null) {
            bindingResult.rejectValue("receiver", "notFound", "존재하지 않는 수신자입니다.");
            return "message_send";  // 수신자가 없으면 폼을 다시 보여줍니다.
        }


        // 메시지 전송
        try {
            messageService.sendMessage(
                    messageForm.getTitle(),
                    messageForm.getContent(),
                    sender,
                    receiver
            );
        } catch (Exception e) {
            bindingResult.reject("messageError", "메시지 전송 중 오류가 발생했습니다.");
            return "message_send";
        }

        // 성공적으로 전송된 경우
        return "redirect:/message/sent"; // 메시지 전송 성공 페이지로 리다이렉트
    }

    @GetMapping("/message/detail/{id}")
    public String showMessageDetail(@PathVariable("id") Long id, Model model, HttpSession session) {

        // 쪽지 조회
        Message message = messageService.findById(id);

        if (message == null) {
            model.addAttribute("error", "쪽지를 찾을 수 없습니다.");
            return "redirect:/message/menu";
        }

        // 쪽지 읽음 처리
        if (!message.isRead()) {
            messageService.markAsRead(message);  // 쪽지 읽음 처리
        }

        model.addAttribute("message", message);
        return "message_detail";  // 쪽지 디테일 페이지로 이동
    }
    //TODO 삭제시 디비에서 완전히 삭제됨
//    @GetMapping("/message/delete/{id}")
//    public String deleteMessage(@PathVariable("id") Long messageId){
//        Message message = messageService.findById(messageId);
//        this.messageService.deleteMessage(message);
//        return "redirect:/message/menu";
//    }
//    @GetMapping("/message/sent/delete/{id}")
//    public String deleteSentMessage(@PathVariable("id") Long messageId){
//        Message message = messageService.findById(messageId);
//        this.messageService.deleteMessage(message);
//        return "redirect:/message/sent";
//    }
}

