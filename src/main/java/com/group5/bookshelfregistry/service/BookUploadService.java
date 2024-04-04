package com.group5.bookshelfregistry.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BookUploadService {
    public String upload(MultipartFile file) ;

    public String update(MultipartFile file, String existingKey);

    public Boolean delete(String existingBookLocation) ;
}
