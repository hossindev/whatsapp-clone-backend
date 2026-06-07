package com.ryzzlab.chatapp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
@Component
public class PresenceService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @EventListener
    public void onDisconnect(SessionDisconnectEvent event){
        String userId = event.getUser() != null ? event.getUser().getName() : null;
        if (userId == null) return;
        stringRedisTemplate.delete("presence:" + userId);
    }
}