package com.example.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.AuthenticationException;
import com.example.exception.DuplicateUsernameException;
import com.example.repository.MessageRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public Account postAccount(@RequestBody Account account) {
        return accountService.addAccount(account);
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateUsernameException(DuplicateUsernameException e) {
        return e.getMessage();
    }
    
    @PostMapping("/login")
    public Account loginAccount(@RequestBody Account account) throws AuthenticationException {
        return accountService.loginAccount(account.getUsername(), account.getPassword());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleAuthenticationEception(AuthenticationException e) {
        return e.getMessage();
    }

    @PostMapping("/messages")
    public Message createMessage(@RequestBody Message message) {
        return messageService.addMessage(message);
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("messages/{messageId}")
    public Message getMessageById(@PathVariable int messageId) {
        return messageService.getMessageById(messageId);
    }

    @DeleteMapping("messages/{messageId}")
    @ResponseStatus(HttpStatus.OK) // This ensures the status is always 200 OK
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        int result = messageService.deleteMessage(messageId);
    
        if (result == 1) {
            return ResponseEntity.ok(1); // Message deleted, return 200 OK with body 1
        } else {
            return ResponseEntity.ok().build(); // Message not found, return 200 OK with empty body
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Object> updateMessageById(@PathVariable int messageId, @RequestBody Message message) {

        try {
            int updatedRows = messageService.updateMessageById(messageId, message.getMessageText());
        
            if (updatedRows == 0) {
                return new ResponseEntity<>("Message not found", HttpStatus.NOT_FOUND);
            }
        
            return new ResponseEntity<>(updatedRows, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("accounts/{accountId}/messages")
    public List<Message> getMessagesByAccountId(@PathVariable int accountId) {
        return messageService.getMessagesByAccountId(accountId);
    }
    

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }





}
