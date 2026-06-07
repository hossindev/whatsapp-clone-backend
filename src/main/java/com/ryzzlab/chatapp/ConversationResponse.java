package com.ryzzlab.chatapp;

import lombok.Data;

@Data
public class ConversationResponse {
    Conversation conversation;
    User otherUser;
}