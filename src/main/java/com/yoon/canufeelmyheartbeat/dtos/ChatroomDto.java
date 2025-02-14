package com.yoon.canufeelmyheartbeat.dtos;
import com.yoon.canufeelmyheartbeat.entities.Chatroom;

import java.time.LocalDateTime;

public record ChatroomDto(
        Long id,
        String title,
        Boolean hasNewMessage,
        Integer memberCount,
        LocalDateTime createdAt) {

    public static ChatroomDto from(Chatroom chatroom) {
        return new ChatroomDto(
                chatroom.getId(),
                chatroom.getTitle(),
                chatroom.getHasNewMessage(),
                chatroom.getMemberChatroomMappingSet().size(),
                chatroom.getCreatedAt()
        );
    }
}
