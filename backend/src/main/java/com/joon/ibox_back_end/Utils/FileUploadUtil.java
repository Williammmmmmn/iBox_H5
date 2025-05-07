package com.joon.ibox_back_end.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * @program: backend
 * @description: 文件上传工具类
 * @author: Joonnn
 * @create: 2025-04-2917:39
 **/

public class FileUploadUtil {
    public static String saveFile(String uploadDir, MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = file.getOriginalFilename(); // 保留原始文件名
        Path filePath = uploadPath.resolve(filename);

        // 强制覆盖已存在文件
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    public static void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }
}
