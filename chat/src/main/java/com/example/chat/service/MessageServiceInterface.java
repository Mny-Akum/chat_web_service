package com.example.chat.service;

import com.example.chat.dao.MessageDao;
import com.example.chat.pojo.Message;
import com.example.chat.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface MessageServiceInterface {

    Result addMessage(Message message);
    Result selectMessage(Message message,Integer pageStart,Integer pageSize);
    Result selectAllMessage(Message message);
}
