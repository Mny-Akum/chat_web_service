package com.example.chat.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {
    private String from_user;
    private String to_user;
    private String message;
    private LocalDateTime send_time;
    private String type;
    private boolean show_time;

    public Message() {
    }

    public Message(String from_user, String to_user, String message, LocalDateTime send_time, String type, boolean show_time) {
        this.from_user = from_user;
        this.to_user = to_user;
        this.message = message;
        this.send_time = send_time;
        this.type = type;
        this.show_time = show_time;
    }
}
