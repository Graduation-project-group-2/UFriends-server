package com.gachon.ufriendsserver;

import com.gachon.ufriendsserver.api.dto.chat.ChatDTO;
import com.gachon.ufriendsserver.api.service.ChatService;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatTest {
    @Autowired
    private ChatService chatService;

    @Test
    public void chat(){
        ChatDTO chatDTO = ChatDTO.builder()
                .userId(1)
                .userChat("오늘 너무 속상했어")
                .build();

        try {
            System.out.println(chatService.sendChatAndReturnChat(chatDTO.getUserId(), chatDTO.getUserChat()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
