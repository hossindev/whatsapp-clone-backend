package com.ryzzlab.chatapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller

public class ChatController {
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ConversationRepository conversationRepository;
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    UserRepository userRepository;


    @MessageMapping("/chat")
    public void sendMessage(@Payload ChatMessage chatMessage, Principal principal){
        Conversation conversation = conversationRepository.findById(chatMessage.getConversationId()).orElseThrow();
        String senderId = principal.getName();
        User sender = userRepository.findById(UUID.fromString(senderId)).orElseThrow();
        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(chatMessage.getContent());
        messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/chat/" + conversation.getId(), message);
    }
    @MessageMapping("/receipt/delivered")
    public  void delivered(@Payload ReceiptMessage receipt, Principal principal){
        UUID messageId = receipt.getMessageId();
        Message message = messageRepository.findById(messageId).orElseThrow();
        message.setStatus("delivered");
        messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/chat/" + message.getConversation().getId(),message);

    }
    @MessageMapping("/receipt/read")
    public void read(@Payload ReceiptMessage receipt, Principal principal){
        List<Message> messages = messageRepository.findByConversationId(receipt.getConversationId());
        String userId = principal.getName();
        for(Message message : messages){
            if (!message.getSender().getId().toString().equals(userId) && !message.getStatus().equals("read")){
                message.setStatus("read");
                messageRepository.save(message);
                messagingTemplate.convertAndSend("/topic/chat/"+ receipt.getConversationId(),message);
            }
        }
    }

}
