package com.yoon.canufeelmyheartbeat.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class HeartbeatController {

    @MessageMapping("/chats") /* 어떤 경로로 publish 한 메세지를 라우팅할 것인지 매핑.  클라이언트는 /pub/chat 으로 메세지를 발행할거야 */
    @SendTo("/sub/chats") /* 클라이언트에게 return 되는 메세지를 보내줄 경로 */
    public String handleMessage(@Payload String message) {
        log.info("메세지 도착함 {}", message);

        return message;
    }
}
