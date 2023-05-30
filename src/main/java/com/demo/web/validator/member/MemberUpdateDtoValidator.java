package com.demo.web.validator.member;

import com.demo.web.dto.member.MemberUpdateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class MemberUpdateDtoValidator implements Validator {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final int MAX_NICKNAME_LENGTH = 10;

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberUpdateDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberUpdateDto memberUpdateDto = (MemberUpdateDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nickName", "required.nickName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.city", "required.address.city");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.street", "required.address.street");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.zipcode", "required.address.zipcode");

        if (memberUpdateDto.getPassword() != null && (memberUpdateDto.getPassword().length() < MIN_PASSWORD_LENGTH
                || memberUpdateDto.getPassword().length() > MAX_PASSWORD_LENGTH)) {
            errors.rejectValue("password", "length.memberUpdateDto.password", "비밀번호는 6자 이상 20자 이하로 입력해주세요.");
        }

        if (memberUpdateDto.getNickName() != null && memberUpdateDto.getNickName().length() > MAX_NICKNAME_LENGTH) {
            errors.rejectValue("nickName", "length.memberUpdateDto.nickName", "닉네임은 10자 이하로 입력해주세요.");
        }

        if (!memberUpdateDto.getPassword().equals(memberUpdateDto.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "password.mismatch", "비밀번호가 일치하지 않습니다.");
        }
    }
}
