package com.demo.web.member.form;

import lombok.Data;

@Data
public class MemberUpdateDto {

    private String name;
    private String nickName;

    public MemberUpdateDto() {
    }

    public MemberUpdateDto(String name, String nickName) {
        this.name = name;
        this.nickName = nickName;
    }
}
