package com.demo.web.dto.member;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MemberJoinDto {

    @NotNull
    private String username;

    @NotNull
    private String password;
    private String confirmPassword;

    @NotNull
    private String name;

    @NotNull
    private String nickName;

}
