package com.demo.web.controller.member.form;

import lombok.Data;

@Data
public class MemberUpdateDto {

    private String password;
    private String name;
    private String nickName;

    public MemberUpdateDto() {
    }

    public MemberUpdateDto(String password, String name, String nickName) {
        this.password = password;
        this.name = name;
        this.nickName = nickName;
    }
}
