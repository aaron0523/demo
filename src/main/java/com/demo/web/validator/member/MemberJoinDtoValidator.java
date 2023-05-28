package com.demo.web.validator.member;

import com.demo.web.dto.member.MemberJoinDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class MemberJoinDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberJoinDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberJoinDto memberJoinDto = (MemberJoinDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "required.username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nickName", "required.nickName");

        if (memberJoinDto.getPassword() != null && memberJoinDto.getPassword().length() < 6
                || memberJoinDto.getPassword().length() > 20) {
            errors.rejectValue("password", "length.memberJoinDto.password");
        }

        if (memberJoinDto.getNickName() != null && memberJoinDto.getNickName().length() > 10) {
            errors.rejectValue("nickName", "length.memberJoinDto.nickName");
        }

        if (!memberJoinDto.getPassword().equals(memberJoinDto.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "password.mismatch");
        }
    }
}