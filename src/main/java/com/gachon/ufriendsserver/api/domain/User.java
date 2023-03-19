package com.gachon.ufriendsserver.api.domain;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "user_id")
    private int userId;

    @Column(nullable = false, unique = true, name = "nickname")
    private String nickname;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private LocalDate createdAt;

    public User setPassword(String password){
        this.password = password;
        return this;
    }
}
