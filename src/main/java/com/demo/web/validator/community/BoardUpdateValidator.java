package com.demo.web.validator.community;

import com.demo.web.dto.board.BoardUpdatedDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class BoardUpdateValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return BoardUpdatedDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BoardUpdatedDto boardUpdateDto = (BoardUpdatedDto) target;

        // 게시글 ID 유효성 검사
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "boardId", "required.boardId");

        // 제목 유효성 검사
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required.title");

        // 내용 유효성 검사
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "content", "required.content");
    }
}
