package com.example.demo.controller;

import com.example.demo.service.OCRService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/ocr")
public class OCRController {
    @Resource
    private OCRService oCRService;

    @PostMapping("/extract-text")
    public ResponseEntity<String> extractTextFromImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("未选择图片!", HttpStatus.BAD_REQUEST);
        }

        try {
            // 将 MultipartFile 转换为 File
            File imageFile = convertMultiPartToFile(file);
            String result = oCRService.extractTextFromImage(imageFile);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("文件处理错误", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }
}