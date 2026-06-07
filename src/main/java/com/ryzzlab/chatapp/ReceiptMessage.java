package com.ryzzlab.chatapp;

import lombok.Data;

import java.util.UUID;

@Data
public class ReceiptMessage {
    UUID messageId;
    UUID conversationId;
}
