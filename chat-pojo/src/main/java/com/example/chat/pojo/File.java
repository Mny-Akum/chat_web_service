package com.example.chat.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    private Long fileSize;
    private String fileName;
    private LocalDateTime uploadTime;
    private Integer type;
    private String uuid;
    private String fromUser;
    private String toUser;
}
