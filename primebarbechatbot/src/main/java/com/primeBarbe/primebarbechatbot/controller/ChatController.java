package com.primeBarbe.primebarbechatbot.controller;


import com.primeBarbe.primebarbechatbot.chatBot.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatBotService chatBotService;

    @PostMapping
    public String conversa(@RequestBody String message){
      return chatBotService.responder(message);
    }
}
