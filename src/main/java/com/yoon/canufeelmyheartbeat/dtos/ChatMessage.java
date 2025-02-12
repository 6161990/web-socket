package com.yoon.canufeelmyheartbeat.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String sender;
    private String message;

    public ChatMessage(String message) {
        this.message = message;
    }
}

