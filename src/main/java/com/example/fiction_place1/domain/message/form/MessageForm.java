package com.example.fiction_place1.domain.message.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageForm {

    @NotBlank(message = "받는 사람의 닉네임 또는 기업명을 입력해주세요.")
    private String receiver;


    @NotBlank(message = "제목은 필수항목입니다.")
    @Size(max = 50, message = "제목은 50자 이내로 작성해주세요.")
    private String title;

    @NotBlank(message = "내용은 필수항목입니다.")
    @Size(max = 1000, message = "내용은 1000자 이내로 작성해주세요.")
    private String content;

}
