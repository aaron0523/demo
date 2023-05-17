package com.demo.web.validator;

import com.demo.web.controller.member.form.MemberUpdateDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class MemberUpdateDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberUpdateDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors, String confirmPassword) {
        MemberUpdateDto memberUpdateDto = (MemberUpdateDto) target;

        // 비밀번호 확인 로직
        if (!memberUpdateDto.getPassword().equals(confirmPassword)) {
            errors.rejectValue("confirmPassword", "password.mismatch", "비밀번호가 일치하지 않습니다.");
        }

        // 다른 필드의 검증 로직
        // ...
    }
}