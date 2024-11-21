package com.example.chat.service.impl;

import com.example.chat.dao.MessageDao;
import com.example.chat.pojo.Message;
import com.example.chat.pojo.MessageList;
import com.example.chat.pojo.Result;
import com.example.chat.service.MessageServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService implements MessageServiceInterface {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    MessageDao messageDao;

    @Override
    public Result addMessage(Message message) {
        int changeCount = messageDao.messageInsert(message);
        log.info("{},数据库添加了一条消息,改变了{}行",System.currentTimeMillis(),changeCount);
        if (changeCount > 0){ return Result.success("添加成功"); }
        return Result.error("添加失败");
    }

    @Override
    public Result selectMessage(Message message,Integer startNum,Integer pageSize) {
        if (startNum == null || startNum <= 0) startNum = 0;
        if (pageSize == null || pageSize <= 0) pageSize = 30;
        List<Message> messages = messageDao.messageSelect(message,startNum,pageSize);
        MessageList messageList = new MessageList(startNum,pageSize,message.getFrom_user(), message.getTo_user(), messages);
        return Result.success(messageList);
    }

    @Override
    public Result selectAllMessage(Message message) {
        List<Message> messages = messageDao.messageSelectAll(message);
        MessageList messageList = new MessageList(message.getFrom_user(), message.getTo_user(), messages);
        return Result.success(messageList);
    }

}
