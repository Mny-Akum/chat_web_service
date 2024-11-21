package com.example.chat.dao;

import com.example.chat.pojo.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageDao {
    //添加消息
    int messageInsert(Message message);
    //查询消息
    List<Message> messageSelectAll(Message message);
    //分页查询消息
    List<Message> messageSelect(Message message,Integer pageStart,Integer pageSize);
}
