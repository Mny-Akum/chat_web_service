package com.example.chat.service;

import com.example.chat.pojo.Result;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileServiceInterface {
    Result fileUpload(MultipartFile[] multipartFiles,Integer type,String from,String to);
    Result imageUpload(MultipartFile multipartFile);
    ResponseEntity<Resource> fileDownload(String uuid, Integer type);
}
