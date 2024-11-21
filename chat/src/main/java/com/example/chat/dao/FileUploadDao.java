package com.example.chat.dao;

import com.example.chat.pojo.File;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileUploadDao {
    int addFiles(List<File> files);
//    单文件上传
    int addFile(File file);
    String getFileName(String uuid,Integer type);
}
