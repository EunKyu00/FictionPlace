package com.example.fiction_place1.domain.user.controller;

import com.example.fiction_place1.domain.user.entity.SiteUser;
import com.example.fiction_place1.domain.user.form.CompanyUserCreateForm;
import com.example.fiction_place1.domain.user.form.SiteUserCreateForm;
import com.example.fiction_place1.domain.user.repository.CompanyUserRepository;
import com.example.fiction_place1.domain.user.repository.SiteUserRepository;
import com.example.fiction_place1.domain.user.service.CompanyUserService;
import com.example.fiction_place1.domain.user.service.SiteUserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class SiteUserController {
    private final SiteUserService siteUserService;
    private final SiteUserRepository siteUserRepository;


    //일반 회원 회원가입 시작
    @GetMapping("/signup/user")
    public String siteUserSignupForm(Model model){
        model.addAttribute("siteUserCreateForm", new SiteUserCreateForm());
        return "site_user_signup";
    }

    @PostMapping("/signup/user")
    public String siteUser(@Valid @ModelAttribute("siteUserCreateForm")
                               SiteUserCreateForm siteUserCreateForm,
                           BindingResult bindingResult){
        // 비번 확인
        if (!siteUserCreateForm.getPassword().equals(siteUserCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "error.password2", "비밀번호가 일치하지 않습니다.");
        }
        // 사용자 이름 유니크 확인
        if (siteUserRepository.existsByUsername(siteUserCreateForm.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "이미 사용 중인 사용자 이름입니다.");
        }
        // 이메일 유니크 확인
        if (siteUserRepository.existsByEmail(siteUserCreateForm.getEmail())) {
            bindingResult.rejectValue("email", "error.email", "이미 사용 중인 이메일입니다.");
        }

        if (bindingResult.hasErrors()){
            return "site_user_signup";
        }
        siteUserService.siteUser(siteUserCreateForm);
        return "redirect:/";
    }
    //일반 회원 회원가입 끝


    @GetMapping("/user/login")
    public String loginForm() {
        return "user_login"; // 로그인 페이지로 이동
    }
    //일반 회원 로그인 시작
    @PostMapping("/user/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        // 입력값 검증
        // 아이디가 비어있는 경우
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("errorUserName", "아이디를 입력해주세요.");
        }
        // 비밀번호가 비어있는 경우
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("errorPassword", "비밀번호를 입력해주세요.");
        }
        // 에러가 있을 경우, 로그인 폼으로 돌아가기
        if (model.containsAttribute("errorUserName") || model.containsAttribute("errorPassword")) {
            return "user_login";
        }
        try {
            // 로그인 시도
            SiteUser user = siteUserService.login(username, password);
            // 세션에 사용자 정보 저장
            session.setAttribute("loginUser", user);
            return "redirect:/"; // 홈으로 리다이렉트
        } catch (IllegalArgumentException e) {
            // 로그인 실패 시 에러 메시지 전달
            model.addAttribute("errorMessage", e.getMessage());
            return "user_login";
        }
    }

    @GetMapping("/user/logout")
    public String logout() {
        return "redirect:/"; // 홈으로 리다이렉트
    }
}
