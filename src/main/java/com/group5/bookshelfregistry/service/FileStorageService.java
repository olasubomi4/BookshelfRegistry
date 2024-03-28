package com.group5.bookshelfregistry.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService implements BookUploadService{
    @Override
    public String uploadBook(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();


            File directory = new File("src/main/resources/book");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            Path filePath = Paths.get(directory.getPath(), fileName);
            Files.write(filePath, file.getBytes());

            return filePath.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    @Override
    public String updateBook(MultipartFile file, String existingKey) {
        return "test";
    }

    @Override
    public Boolean deleteBook(String existingBookLocation){
        return true;
    }
}
