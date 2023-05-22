package com.gachon.ufriendsserver.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "chat_id")
    private int chatId;

    @Column(nullable = false, name = "user_id")
    private int userId;

    @Column(nullable = false, name = "user_chat")
    private String userChat;

    @Column(nullable = false, name = "bot_chat")
    private String botChat;

    @CreationTimestamp
    @JsonFormat(pattern="yyyy.MM.dd/HH:mm/E")
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;
}
