package com.ryzzlab.chatapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller

public class ChatController {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ConversationRepository conversationRepository;
    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    public void sendMessage(@Payload ChatMessage chatMessage, Principal principal){
        Conversation conversation = conversationRepository.findById(chatMessage.getConversationId()).orElseThrow();
        String senderId = principal.getName();
        User sender = new User();
        sender.setId(UUID.fromString(senderId));

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(chatMessage.getContent());
        messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/chat/" + conversation.getId(), message);
    }
}
