package com.ryzzlab.chatapp;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.Id;
@Entity
@Table(name = "conversation_members")
@Data
@IdClass(ConversationMemberId.class)
public class ConversationMember {
    @Id
    @ManyToOne
    @JoinColumn(name = "conversation_id")
    Conversation conversation;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
