package com.ryzzlab.chatapp;

import com.ryzzlab.chatapp.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    //signup
    @Autowired
    private UserRepository userRepository;
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body){
        String email = body.get("email");
        String password = body.get("password");
        String displayName = body.get("displayName");
        if (userRepository.findByEmail(email).isPresent()){
            return ResponseEntity.badRequest().body("Email already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder().encode(password));
        user.setDisplayName(displayName);
        userRepository.save(user);
        return ResponseEntity.ok("User registered");
    }

    //Login
    @Autowired
    private JwtService jwtService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body){
        String email = body.get("email");
        String password = body.get("password");
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        User user = userOptional.get();
        if(!passwordEncoder().matches(password, user.getPassword())){
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        String token = jwtService.generateToken(user.getId().toString());
        return ResponseEntity.ok(Map.of("token",token));
    }

}
