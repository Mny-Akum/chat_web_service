package com.example.chat.service.impl;

import com.example.chat.dao.FileUploadDao;
import com.example.chat.pojo.File;
import com.example.chat.pojo.Result;
import com.example.chat.service.FileServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileService implements FileServiceInterface{
    @Value("${store.file}")
    private String fileStorePath;
    @Value("${store.image}")
    private String imageStorePath;
    private final FileUploadDao fileUploadDao;

    public FileService(FileUploadDao fileUploadDao) {
        this.fileUploadDao = fileUploadDao;
    }

    @PostConstruct
    public void init(){
        java.io.File directory1 = new java.io.File(imageStorePath);
        java.io.File directory2 = new java.io.File(imageStorePath);
        if (!directory1.isDirectory()){directory1.mkdirs();}
        if (!directory2.isDirectory()){directory2.mkdirs();}
    }

    public Result fileUpload(MultipartFile[] multipartFiles,Integer type,String from,String to){
        List<File> files = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        for (MultipartFile multipartFile : multipartFiles){
            String filename = multipartFile.getOriginalFilename();
            long fileSize = multipartFile.getSize();
            if (fileSize == 0){
                return Result.error("传输文件大小不能为0");
            }
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String path = "";
            if (type == 0){
                path = fileStorePath+'\\'+uuid;
            } else if (type == 1) {
                path = imageStorePath+'\\'+uuid;
            }
            //文件传输
            if (storeFile(multipartFile, path)) return Result.error("文件传输失败");
            files.add(new File(fileSize,filename,localDateTime,type,uuid,from,to));
        }
        fileUploadDao.addFiles(files);
        return Result.success(files);
    }

    @Override
    public Result imageUpload(MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();
        long fileSize = multipartFile.getSize();
        if (fileSize == 0){
            return Result.error("传输文件大小不能为0");
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String path = imageStorePath+'\\'+uuid;
        if (storeFile(multipartFile, path)) return Result.error("文件传输失败");
        File file = new File(fileSize,filename,LocalDateTime.now(),2,uuid,null,null);
        int i = fileUploadDao.addFile(file);
        if (i>0){
            return Result.success(file);
        }else {
            return Result.error("文件传输失败");
        }
    }

    private boolean storeFile(MultipartFile multipartFile, String path) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
            byte[] bytes = new byte[1024];
            int len = -1;
            InputStream inputStream = multipartFile.getInputStream();
            while ((len = inputStream.read(bytes)) != -1){
                fileOutputStream.write(bytes,0,len);
            }
        } catch (IOException e) {
            log.error("文件传输错误：{}",e.getMessage());
            return true;
        }
        return false;
    }

    @Override
    public ResponseEntity<Resource> fileDownload(String uuid, Integer type) {
        String fileName = fileUploadDao.getFileName(uuid, type);
        //防止特殊字符而识别不出
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        //判断是否存在该文件
        if (fileName.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Path filePath = null;
        if (type==2){
            filePath = Paths.get(imageStorePath).resolve(uuid).normalize();
        }else{
            filePath = Paths.get(fileStorePath).resolve(uuid).normalize();
        }


        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()||resource.isReadable()){
                // 动态获取内容类型
                String contentType;
                try{
                    contentType = Files.probeContentType(filePath);
                    if (contentType == null){
                        contentType = "application/octet-stream";
                    }
                }catch (IOException e){
                    contentType = "application/octet-stream";
                }


                HttpHeaders headers = new HttpHeaders();
                //添加文件进请求
                headers.add(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+encodedFileName+"\"");
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).headers(headers).body(resource);
            }else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
