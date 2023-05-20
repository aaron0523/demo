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

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles, BoardType boardType) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile, boardType));
            }
        }
        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile, BoardType boardType) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String storeFileName = createStoreFileName(originalFilename);
        String filePath = getFilePath(storeFileName, boardType);

        Files.copy(multipartFile.getInputStream(), Path.of(filePath), StandardCopyOption.REPLACE_EXISTING);

        return new UploadFile(originalFilename, storeFileName, boardType);
    }

    public void deleteFile(String fileName, BoardType boardType) throws IOException {
        String filePath = getFilePath(fileName, boardType);
        Files.deleteIfExists(Path.of(filePath));
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
