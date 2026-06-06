package com.ryzzlab.chatapp;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class ConversationMemberId implements Serializable {
    Conversation conversation;
    User user;
}
