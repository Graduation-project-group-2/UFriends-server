package com.gachon.ufriendsserver.api.controller;

import com.gachon.ufriendsserver.api.common.controller.CommonController;
import com.gachon.ufriendsserver.api.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController extends CommonController {
    private final ChatService chatService;

    @SneakyThrows
    @GetMapping
    public ResponseEntity sendChatAndReturnChat(@RequestParam int userId, @RequestParam String prompt){
        return SuccessReturn(chatService.sendChatAndReturnChat(userId, prompt));
    }

}
