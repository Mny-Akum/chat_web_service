package com.example.chat.controller;

import com.example.chat.pojo.Message;
import com.example.chat.pojo.Result;
import com.example.chat.service.impl.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/chat/message")
public class MessageController {
    @Autowired
    MessageService messageService;
    @GetMapping("/list")
    public Result getMessage(String from, String to, String message, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime time, String type,Integer startNum,Integer pageSize){
        return messageService.selectMessage(new Message(from,to,message,time,type,true),startNum,pageSize);
    }
    @GetMapping("/allList")
    public Result getAllMessage(String from, String to, String message, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime time, String type){
        return messageService.selectAllMessage(new Message(from,to,message,time,type,true));
    }
}
