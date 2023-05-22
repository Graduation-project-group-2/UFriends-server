package com.gachon.ufriendsserver.api.service;

import com.gachon.ufriendsserver.api.domain.Chat;
import com.gachon.ufriendsserver.api.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;

    public Chat sendChatAndReturnChat(int userId, String prompt) throws ParseException {
        // Flask URL
        String urlString = null;
        try {
            urlString = "http://localhost:5000/chat-to-chatbot?prompt=" + URLEncoder.encode(prompt, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("[Chat Service]: " + "urlString = " + urlString);

        URL url = null;
        HttpURLConnection con = null;
        JSONObject result = null;
        StringBuilder sb = new StringBuilder();
        try {
            url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-type", "application/json");
            con.setDoOutput(true);

            // 데이터 입력 스트림에 담기
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            while (br.ready()) {
                sb.append(br.readLine());
            }
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        result = (JSONObject) new JSONParser().parse(sb.toString());
        String chatbotSay = result.get("data").toString();

        log.info("[Chat Service]: " + "chatbotSay = " + chatbotSay);

//        out.append(result.get("code") + " : " + result.get("msg") + "\n");
        int startIndex = chatbotSay.indexOf("{\"result\":\"");
        int endIndex = chatbotSay.indexOf("\"}");


        return chatRepository.save(
                Chat.builder()
                        .userId(userId)
                        .userChat(prompt)
                        .botChat(chatbotSay.substring(startIndex + 11, endIndex))
                        .build()
        );
    }

}
