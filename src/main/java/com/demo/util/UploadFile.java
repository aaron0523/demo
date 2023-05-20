package com.demo.util;

import com.demo.domain.board.Board;
import com.demo.domain.board.BoardType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "upload_file")
@Getter
@Setter
public class UploadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String uploadFileName; // 고객이 올린 파일 이름
    private String storeFileName; // 시스템에 저장한 파일 이름

    @Enumerated(EnumType.STRING)
    private BoardType boardType; // 게시판 유형

    public UploadFile(String uploadFileName, String storeFileName, BoardType boardType) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.boardType = boardType;
    }

    public UploadFile() {

    }
}
