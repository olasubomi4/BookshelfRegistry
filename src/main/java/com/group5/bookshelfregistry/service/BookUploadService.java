package com.group5.bookshelfregistry.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BookUploadService {
    public String uploadBook(MultipartFile file) ;
}
