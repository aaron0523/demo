package com.demo.web.dto.member;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class MemberUpdateDto {

    private Long id;
    private String username;

    @NotNull
    @Size(min = 6, max = 20)
    private String password;
    private String confirmPassword;

    @NotNull
    @Size(max = 30)
    private String name;

    @NotNull
    @Size(max = 10)
    private String nickName;

    private LocalDateTime updatedDate;

    public MemberUpdateDto() {
    }

}
