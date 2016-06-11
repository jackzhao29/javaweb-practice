package com.coldface.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.coldface.service.MyService;

/**
 * 类ChatController.java的实现描述：WebSocket业务逻辑
 * 
 * @author coldface
 * @date 2016年2月21日下午6:57:18
 */
@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private MyService             myService;

    @MessageMapping("/hello")
    public String send(String message) {

        System.out.println("simple--->chatController" + message);
        String text = "[" + System.currentTimeMillis() + "]:" + "您好！" + message + myService.process(message);

        template.convertAndSend("/topic/greetings", text);

        return text;
    }

    //接受socket消息，并给clien发送消息
    @MessageMapping("/hello2")
    @SendTo("/topic/send")
    public Long push(String message) throws Exception {
        System.out.println(".....server...Accept..." + message);
        System.out.println(".....server..SendTo..");
        return System.currentTimeMillis();
    }
}
