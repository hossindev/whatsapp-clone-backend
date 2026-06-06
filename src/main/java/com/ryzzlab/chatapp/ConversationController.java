package com.ryzzlab.chatapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(conversation);
    }
}
