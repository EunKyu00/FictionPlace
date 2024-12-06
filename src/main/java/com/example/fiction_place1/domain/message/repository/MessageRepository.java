package com.example.fiction_place1.domain.message.repository;

import com.example.fiction_place1.domain.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Long> {
}
