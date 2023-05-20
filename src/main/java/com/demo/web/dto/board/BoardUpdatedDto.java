package com.demo.web.dto.board;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BoardUpdatedDto {
    private Long id;
    private String title;
    private String content;
    private MultipartFile[] attachments;
    private String youtubeUrl;
    private String boardType;
}