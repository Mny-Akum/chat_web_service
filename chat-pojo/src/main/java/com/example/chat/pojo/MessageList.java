package com.example.chat.pojo;

import lombok.Data;

import java.util.List;

@Data
public class MessageList {
    private Integer startNum;
    private Integer pageSize;
    private Integer count;
    private String from;
    private String to;
    private List<Message> messageList;

    public MessageList( String from, String to, List<Message> messageList) {
        this.count = messageList.size();
        this.from = from;
        this.to = to;
        this.messageList = messageList;
    }

    public MessageList(Integer startNum, Integer pageSize, String from, String to, List<Message> messageList) {
        this.startNum = startNum;
        this.pageSize = pageSize;
        this.count = messageList.size();
        this.from = from;
        this.to = to;
        this.messageList = messageList;
    }
}
