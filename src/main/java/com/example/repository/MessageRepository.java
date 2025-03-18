package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findByMessageId(int messageId);
    void deleteBymessageId(int messageId);
    Boolean existsByMessageId(int messageId);
    List<Message> findByPostedBy(int accountId);
}
