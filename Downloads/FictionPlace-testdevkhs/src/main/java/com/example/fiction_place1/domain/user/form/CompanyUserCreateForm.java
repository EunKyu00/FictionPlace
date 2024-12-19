package com.example.fiction_place1.domain.user.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyUserCreateForm {
    @NotBlank(message = "회사 이름은 필수입니다.")
    private String companyName;

    @NotBlank(message = "사업자 등록번호는 필수입니다.")
    private String businessRegistrationNumber;

    @NotBlank(message = "담당자 이메일은 필수입니다.")
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String password2;

    @NotBlank(message = "담당자 이름은 필수입니다.")
    private String contactPerson;
}
