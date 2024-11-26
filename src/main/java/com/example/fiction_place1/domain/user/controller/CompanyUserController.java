package com.example.fiction_place1.domain.user.controller;

import com.example.fiction_place1.domain.user.entity.CompanyUser;
import com.example.fiction_place1.domain.user.form.CompanyUserCreateForm;
import com.example.fiction_place1.domain.user.repository.CompanyUserRepository;
import com.example.fiction_place1.domain.user.service.CompanyUserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CompanyUserController {
    private final CompanyUserRepository companyUserRepository;
    private final CompanyUserService companyUserService;

    //기업 회원 회원가입 시작
    @GetMapping("/signup/company")
    public String companyUserSignupForm(Model model){
        model.addAttribute("companyUserCreateForm", new CompanyUserCreateForm());
        return "company_user_signup";
    }

    @PostMapping("/signup/company")
    public String companyUser(@Valid @ModelAttribute("companyUserCreateForm")
                              CompanyUserCreateForm companyUserCreateForm,
                              BindingResult bindingResult){

        //비번 확인
        if (!companyUserCreateForm.getPassword().equals(companyUserCreateForm.getPassword2())){
            bindingResult.rejectValue("password2","error.password2","비밀번호가 일치하지 않습니다.");
        }
        //기업 이름 유니크 확인
        if(companyUserRepository.existsByCompanyName(companyUserCreateForm.getCompanyName())){
            bindingResult.rejectValue("companyName","error.companyName","이미 사용 중인 기업이름 입니다.");
        }
        //이메일 유니크 확인
        if (companyUserRepository.existsByEmail(companyUserCreateForm.getEmail())){  // 여기에 existsByEmail 사용
            bindingResult.rejectValue("email","error.email","이미 사용 중인 이메일입니다.");
        }
        if (bindingResult.hasErrors()){
            return "company_user_signup";
        }
        companyUserService.companyUser(companyUserCreateForm);

        return "redirect:/";
    }

    //기업 로그인 시작

    @GetMapping("/login/company")
    public String loginForm() {
        return "company_login";
    }

    @PostMapping("/login/company")
    public String login(@RequestParam("companyName") String companyName,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        // 입력값 검증
        // 아이디가 비어있는 경우
        if (companyName == null || companyName.trim().isEmpty()) {
            model.addAttribute("errorCompanyName", "아이디를 입력해주세요.");
        }

        // 비밀번호가 비어있는 경우
        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("errorPassword", "비밀번호를 입력해주세요.");
        }

        // 에러가 있을 경우, 로그인 폼으로 돌아가기
        if (model.containsAttribute("errorCompanyName") || model.containsAttribute("errorPassword")) {
            return "company_login";
        }

        try {
            // 로그인 시도
            CompanyUser companyUser = companyUserService.login(companyName, password);
            // 세션에 사용자 정보 저장
            session.setAttribute("loginCompanyUser", companyUser);

            return "redirect:/";
        } catch (IllegalArgumentException e) {
            // 로그인 실패 시 에러 메시지 전달
            model.addAttribute("errorMessage", e.getMessage());
            return "company_login";
        }
    }


    @GetMapping("/company/logout")
    public String logout() {
        return "redirect:/";
    }
}
