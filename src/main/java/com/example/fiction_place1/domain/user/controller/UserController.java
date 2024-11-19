package com.example.fiction_place1.domain.user.controller;

import com.example.fiction_place1.domain.user.form.CompanyUserCreateForm;
import com.example.fiction_place1.domain.user.form.SiteUserCreateForm;
import com.example.fiction_place1.domain.user.repository.CompanyUserRepository;
import com.example.fiction_place1.domain.user.repository.SiteUserRepository;
import com.example.fiction_place1.domain.user.service.CompanyUserService;
import com.example.fiction_place1.domain.user.service.SiteUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final SiteUserService siteUserService;
    private final CompanyUserService companyUserService;
    private final SiteUserRepository siteUserRepository;
    private final CompanyUserRepository companyUserRepository;


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
}
