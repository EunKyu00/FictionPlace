package com.example.fiction_place1.domain.webtoon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private static final String UPLOAD_DIR = "C:/uploads"; // 로컬 파일 저장 경로

    public String uploadImage(MultipartFile file) throws IOException {
        // 파일 이름 생성
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path targetPath = Paths.get(UPLOAD_DIR, fileName);

        // 디렉토리 확인 및 생성
        if (!Files.exists(targetPath.getParent())) {
            Files.createDirectories(targetPath.getParent());
        }

        // 파일 저장
        Files.copy(file.getInputStream(), targetPath);

        // HTTP로 접근할 수 있는 URL 반환
        return "/uploads/" + fileName;
    }
}



