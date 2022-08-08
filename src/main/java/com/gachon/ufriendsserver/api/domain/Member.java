package com.gachon.ufriendsserver.api.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "MEMBER_ID")
    private int memberId;

    @Column(nullable = false, unique = true, name = "NICKNAME")
    private String nickname;

    @Column(nullable = false, unique = true, name = "EMAIL")
    private String email;

    @Column(nullable = false, name = "PASSWORD")
    private String password;

    @Column(nullable = false, unique = true, name = "PHONE_NUM")
    private String phoneNum;

    @Column(nullable = false, name = "BIRTHDAY")
    private LocalDate birthday;

    @CreationTimestamp
    @Column(nullable = false, name = "JOIN_DATE")
    private LocalDate joinDate;

    @Column(nullable = true, name="SOCIAL")
    @Enumerated(EnumType.STRING)
    private Social social;
}
