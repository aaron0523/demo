package com.demo.web.validator;

import com.demo.web.dto.member.MemberUpdateDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


public class MemberUpdateDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberUpdateDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberUpdateDto memberUpdateDto = (MemberUpdateDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required.memberUpdateDto.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required.memberUpdateDto.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nickName", "required.memberUpdateDto.nickName");

        if (memberUpdateDto.getPassword() != null && memberUpdateDto.getPassword().length() < 6
                || memberUpdateDto.getPassword().length() > 20) {
            errors.rejectValue("password", "length.memberUpdateDto.password");
        }

        if (memberUpdateDto.getNickName() != null && memberUpdateDto.getNickName().length() > 10) {
            errors.rejectValue("nickName", "length.memberUpdateDto.nickName");
        }

        // 비밀번호 확인 로직
        if (!memberUpdateDto.getPassword().equals(memberUpdateDto.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "password.mismatch");
        }
    }
}