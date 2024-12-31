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
    public String messageMenu(Model model, HttpSession session) {
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        if (siteUser == null && companyUser == null){
            return "access_denied";
        }

        if (siteUser != null) {
            List<Message> messages = messageService.getReceiverSiteUserMessage(siteUser);
            model.addAttribute("messages",messages);
        }
        if (companyUser != null){
            List<Message> messages = messageService.getReceiverCompanyUserMessage(companyUser);
            model.addAttribute("messages",messages);
        }

        return "message_menu";
    }

    //보낸 쪽지
    @GetMapping("/message/sent")
    public String sentMessages(Model model, HttpSession session) {
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");

        if (siteUser == null && companyUser == null) {
            return "access_denied";
        }

        if (siteUser != null) {
            List<Message> messages = messageService.getSenderSiteUserMessage(siteUser);
            model.addAttribute("messages", messages);

            // 읽지 않은 쪽지 수를 계산하여 표시
            long unreadCount = messages.stream().filter(message -> !message.isRead()).count();
            model.addAttribute("unreadCount", unreadCount);
        }

        if (companyUser != null) {
            List<Message> messages = messageService.getSenderCompanyMessage(companyUser);
            model.addAttribute("messages", messages);

            // 읽지 않은 쪽지 수를 계산하여 표시
            long unreadCount = messages.stream().filter(message -> !message.isRead()).count();
            model.addAttribute("unreadCount", unreadCount);
        }


        return "message_sent";  // fallback, 일반적으로는 이 부분까지 오지 않음
    }


    @GetMapping("/message/send")
    public String sendMessage(Model model,HttpSession session){
        model.addAttribute("messageForm", new MessageForm());
        SiteUser siteUser = (SiteUser) session.getAttribute("loginUser");
        CompanyUser companyUser = (CompanyUser) session.getAttribute("loginCompanyUser");
        if (siteUser != null) {
            model.addAttribute("user", siteUser);
        } else if (companyUser != null) {
            model.addAttribute("companyUser", companyUser);
        }
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
}

