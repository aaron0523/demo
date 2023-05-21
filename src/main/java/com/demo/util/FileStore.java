package com.demo.util;

import com.demo.domain.board.BoardType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles, BoardType boardType) throws IOException {
        return multipartFiles.stream()
                .filter(multipartFile -> !multipartFile.isEmpty())
                .map(multipartFile -> {
                    try {
                        return storeFile(multipartFile, boardType);
                    } catch (IOException e) {
                        throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
                    }
                })
                .collect(Collectors.toList());

    }

    public UploadFile storeFile(MultipartFile multipartFile, BoardType boardType) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        // StringUtils.cleanPath() : 상위 디렉토리 표시(../)나 현재 디렉토리 표시(./) 등을 처리하고, 경로 구분자를 표준화하여 반환
        String originalFilename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        // createStoreFileName() : 원본 파일 이름에서 파일 확장자(extension)를 추출한 후,
        // UUID(Universally Unique Identifier)를 생성하여 파일 이름과 결합
        String storeFileName = createStoreFileName(originalFilename);
        String filePath = getFilePath(storeFileName, boardType);

        Files.copy(multipartFile.getInputStream(), Path.of(filePath), StandardCopyOption.REPLACE_EXISTING);

        return new UploadFile(originalFilename, storeFileName, boardType);
    }

    public void deleteFile(String fileName, BoardType boardType) throws IOException {
        String filePath = getFilePath(fileName, boardType);
        if (Files.exists(Path.of(filePath))) {
            Files.delete(Path.of(filePath));
        }
    }

    private String createStoreFileName(String originalFilename) {
        String ext = getFileExtension(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String getFilePath(String fileName, BoardType boardType) {
        return fileDir + File.separator + boardType.name().toLowerCase() + File.separator + fileName;
    }
}
