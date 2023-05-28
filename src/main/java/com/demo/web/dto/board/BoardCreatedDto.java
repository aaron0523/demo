package com.demo.web.dto.board;

import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

@Data
public class BoardCreatedDto {
    private Long id;
    private String boardType;
    private String title;
    private String content;
//    private MultipartFile attachFile; 파일 첨부가 필수적인 요소일 경우 사용
    private MultipartFile[] attachments;
    private String youtubeUrl;
}
