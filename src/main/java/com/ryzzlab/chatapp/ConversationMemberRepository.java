package com.ryzzlab.chatapp;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.UUID;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, ConversationMemberId> {
    List<ConversationMember> findByUser_Id(UUID userId);
    @Query("SELECT cm.conversation FROM ConversationMember cm WHERE cm.user.id = :userId")
    List<Conversation> findConversationsByUserId(@Param("userId") UUID userId);
}