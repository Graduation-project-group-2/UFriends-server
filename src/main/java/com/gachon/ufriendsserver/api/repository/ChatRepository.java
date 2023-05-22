package com.gachon.ufriendsserver.api.repository;

import com.gachon.ufriendsserver.api.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
}
