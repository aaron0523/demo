package com.demo.web.dto.member;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MemberJoinDto {

//    @NotNull
    private String username;

//    @NotNull
    private String password;
    private String confirmPassword;

//    @NotNull
    private String name;

//    @NotNull
    private String nickName;

}
