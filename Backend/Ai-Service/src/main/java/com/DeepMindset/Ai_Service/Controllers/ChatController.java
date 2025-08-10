package com.DeepMindset.Ai_Service.Controllers;

import com.DeepMindset.Ai_Service.Services.ChatService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/ask")
    public ResponseEntity<?> AiChatBot(@RequestParam String userQuery){
        try {
            return ResponseEntity.ok(chatService.ChatWithAi(userQuery));
        }catch (IllegalArgumentException i){
            return ResponseEntity.badRequest().body("Empty userQuery");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }
}
