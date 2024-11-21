package com.example.chat.controller;

import cn.hutool.core.io.FileUtil;
import com.example.chat.pojo.Result;
import com.example.chat.service.impl.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/chat")
public class FileController {
    @Autowired
    FileService fileService;
    //用于上传文件
    @PostMapping("/upload/file")
    public Result fileUpload(@RequestParam("file") MultipartFile[] multipartFiles,Integer type,String from,String to){
        return fileService.fileUpload(multipartFiles,type,from,to);
    }
    @GetMapping("/download/{type}/{uuid}")
    public ResponseEntity<Resource> downLoad(@PathVariable String uuid, @PathVariable Integer type){
        return fileService.fileDownload(uuid,type);
    }
    //上传头像
    @PostMapping("/upload/image")
    public Result imageUpload(@RequestParam("image") MultipartFile multipartFile){
        return fileService.imageUpload(multipartFile);
    }
}
