package com.ryzzlab.chatapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    @Autowired
    ConversationRepository conversationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    ConversationMemberRepository conversationMemberRepository;

    @PostMapping()
    public ResponseEntity<?> get_conversations(@RequestBody Map<String, String> body, @RequestHeader("Authorization") String authHeader){
        String userId = jwtService.extractUserId(authHeader.substring(7));
        String query = body.get("query");
        Optional<User> otherUser = userRepository.findByEmail(query);
        if(otherUser.isEmpty()){
            otherUser = userRepository.findByDisplayName(query);

        }
        if(otherUser.isEmpty()){
            return ResponseEntity.badRequest().body("User not found");
        }

        Conversation conversation = new Conversation();
        conversation.setGroup(false);
        conversationRepository.save(conversation);
        User currentUser = new User();
        currentUser.setId(UUID.fromString(userId));

        ConversationMember member1 = new ConversationMember();
        member1.setConversation(conversation);
        member1.setUser(currentUser);
        conversationMemberRepository.save(member1);

        ConversationMember member2 = new ConversationMember();
        member2.setConversation(conversation);
        member2.setUser(otherUser.get());
        conversationMemberRepository.save(member2);

        return ResponseEntity.ok(conversation);
    }
    @GetMapping
    public ResponseEntity<?> getConversations(@RequestHeader("Authorization") String authHeader){
        String userId = jwtService.extractUserId(authHeader.substring(7));
        List<ConversationMember> memberships = conversationMemberRepository.findByUser_Id(UUID.fromString(userId));
        List<Conversation> conversations = memberships.stream()
                .map(ConversationMember::getConversation)
                .toList();
        return ResponseEntity.ok(conversations);
    }
}
