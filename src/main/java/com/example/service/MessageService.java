package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message addMessage(Message message) {

        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Invalid format");
        }

        Account account = accountRepository.findByAccountId(message.getPostedBy());

        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
       
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int messageId) {
        return messageRepository.findByMessageId(messageId);
    }

    public int deleteMessage(int messageId) {
        Message message = messageRepository.findByMessageId(messageId);
    
        if (message != null) {
            messageRepository.delete(message);
            return 1; // Message deleted
        } else {
            return 0; // Message not found
        }
    }

    public int updateMessageById(int messageId, String newMessageText) {

        Message message = messageRepository.findByMessageId(messageId);
        if (message == null) {
            throw new IllegalArgumentException("Message Id not found.");
        }

        if (newMessageText == null || newMessageText.isBlank() || newMessageText.trim().isEmpty()) {
            throw new IllegalArgumentException("New message text cannot be blank.");
        }

        if (newMessageText.equals("")) {
            throw new IllegalArgumentException("pleaseee");

        }
    
        // Ensure newMessageText is not too long
        if (newMessageText.length() > 255) {
            throw new IllegalArgumentException("New message text is too long.");
        }

        message.setMessageText(newMessageText);
        messageRepository.save(message);

        return 1;
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
