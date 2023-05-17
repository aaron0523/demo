package com.demo.web.controller.member.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class MemberUpdateDto {

    @NotNull(message = "비밀번호는 필수 입력 항목입니다.")
    @Length(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하로 입력해야 합니다.")
    private String password;

    @NotNull(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotNull(message = "별명은 필수 입력 항목입니다.")
    @Length(max = 30, message = "별명은 30자 이하로 입력해야 합니다.")
    private String nickName;

    public MemberUpdateDto() {
    }

    public MemberUpdateDto(String password, String name, String nickName) {
        this.password = password;
        this.name = name;
        this.nickName = nickName;
    }
}
