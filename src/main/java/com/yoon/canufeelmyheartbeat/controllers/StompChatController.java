package com.yoon.canufeelmyheartbeat.controllers;

import com.yoon.canufeelmyheartbeat.dtos.ChatMessage;
import com.yoon.canufeelmyheartbeat.entities.Message;
import com.yoon.canufeelmyheartbeat.services.ChatService;
import com.yoon.canufeelmyheartbeat.vos.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chats/{chatroomId}") /* 어떤 경로로 publish 한 메세지를 라우팅할 것인지 매핑.  클라이언트는 /pub/chat 으로 메세지를 발행할거야 */
    @SendTo("/sub/chats/{chatroomId}") /* 클라이언트에게 return 되는 메세지를 보내줄 경로 */
    public ChatMessage handleMessage(@AuthenticationPrincipal Principal principal, @DestinationVariable Long chatroomId, @Payload Map<String, String> payload) {
        log.info("StompController handleMessage {} 메세지 도착함 {}, {}", principal.getName(), chatroomId, payload);
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) ((OAuth2AuthenticationToken) principal).getPrincipal();

        chatService.saveMessage(customOAuth2User.getMember(), chatroomId, payload.get("message"));

        messagingTemplate.convertAndSend("/sub/chats/news", chatroomId);

        return new ChatMessage(principal.getName(), payload.get("message"));
    }

   }
