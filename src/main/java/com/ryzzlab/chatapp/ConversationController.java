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
    @Autowired
    MessageRepository messageRepository;

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
        List<Conversation> user_conv = conversationMemberRepository.findConversationsByUserId(UUID.fromString(userId));
        List<Conversation> other_user_conv = conversationMemberRepository.findConversationsByUserId(otherUser.get().getId());
        Optional<Conversation> existing = user_conv.stream()
                .filter(other_user_conv::contains)
                .findFirst();

        if (existing.isPresent()){
            return ResponseEntity.ok(existing.get());
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
    @GetMapping("/{id}/messages")
    public ResponseEntity<?> getMessages(@PathVariable UUID id, @RequestHeader("Authorization") String authHeader) {
        List<Message> messages = messageRepository.findByConversationId(id);
        return ResponseEntity.ok(messages);
    }
    @PostMapping("/group")
    public ResponseEntity<?>  create_group(@RequestBody CreateGroupRequest body, @RequestHeader("Authorization") String authHeader ){
        String userId = jwtService.extractUserId(authHeader.substring(7));
        Conversation conversation = new Conversation();
        conversation.setGroup(true);
        conversation.setName(body.getName());
        conversationRepository.save(conversation);

        User currentUser = new User();
        currentUser.setId(UUID.fromString(userId));
        ConversationMember me = new ConversationMember();
        me.setConversation(conversation);
        me.setUser(currentUser);
        conversationMemberRepository.save(me);
        for ( UUID memberId : body.getMemberIds()) {
                User u = new User();
                u.setId(memberId);
                ConversationMember member = new ConversationMember();
                member.setConversation(conversation);
                member.setUser(u);
                conversationMemberRepository.save(member);
            }
        return  ResponseEntity.ok(conversation);
        }
    }

