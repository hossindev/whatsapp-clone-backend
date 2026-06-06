package com.ryzzlab.chatapp;

import lombok.Data;

import java.util.UUID;

@Data
public class ChatMessage {
    UUID conversationId;
    String content;
}
