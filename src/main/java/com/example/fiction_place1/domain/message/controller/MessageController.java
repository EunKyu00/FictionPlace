package com.example.fiction_place1.domain.message.controller;

import com.example.fiction_place1.domain.board_type.entity.BoardType;
import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.entity.SiteUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

//@Controller
//@RequiredArgsConstructor
//public class MessageController {
//    private final MessageService messageService;
//
//    @GetMapping("/send/message")
//    public String sendMessage(){
//        return "message_form";
//    }
//}
