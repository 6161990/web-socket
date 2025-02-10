package com.yoon.canufeelmyheartbeat.controllers;

import com.yoon.canufeelmyheartbeat.dtos.ChatMessage;
import com.yoon.canufeelmyheartbeat.services.DateRequestBroadcastor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HeartbeatController {

    private final DateRequestBroadcastor dateRequestBroadcastor;

    @MessageMapping("/chats") /* 어떤 경로로 publish 한 메세지를 라우팅할 것인지 매핑.  클라이언트는 /pub/chat 으로 메세지를 발행할거야 */
    @SendTo("/sub/chats") /* 클라이언트에게 return 되는 메세지를 보내줄 경로 */
    public ChatMessage handleMessage(@AuthenticationPrincipal Principal principal, @Payload Map<String, String> payload) {
        log.info("{} 메세지 도착함 {}", principal.getName(), payload);

        return new ChatMessage(principal.getName(), payload.get("message"));
    }

    @MessageMapping("/date-request")
    /*@SendTo("/sub/date-request-alarm") 스트림즈가 직접 전송하기 때문에 불필요 */
    public void handleEvent(@AuthenticationPrincipal Principal principal, @Payload Map<String, String> payload) {
        log.info("{} handleEvent 메세지 도착함 {}", principal.getName(), payload);

        dateRequestBroadcastor.broadcast(principal.getName(), payload);
    }
}
