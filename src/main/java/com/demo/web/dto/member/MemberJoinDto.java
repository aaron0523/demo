package com.demo.web.dto.member;

import lombok.Data;


@Data
public class MemberJoinDto {

//    @NotNull
    private String username;

    private String password;
    private String confirmPassword;

    private String name;

    private String nickName;

    private String city;
    private String street;
    private String zipcode;

}
