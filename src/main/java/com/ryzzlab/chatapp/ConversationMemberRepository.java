package com.ryzzlab.chatapp;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationMemberRepository extends JpaRepository<ConversationMember, ConversationMemberId> {
}