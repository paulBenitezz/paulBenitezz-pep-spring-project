package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.AuthenticationException;
import com.example.exception.DuplicateUsernameException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /*Our API should be able to process new User registrations. */
    public Account addAccount(Account account){
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Invalid username or password. Account not saved.");
        }

        if (accountRepository.findByUsername(account.getUsername()) != null) {
            throw new DuplicateUsernameException("Username already exists.");
        }


        return accountRepository.save(account);
    }

    public Account loginAccount(String username, String password) throws AuthenticationException {

        if (username.isBlank() || password.isBlank()) {
            throw new AuthenticationException("Username and password required");
        }

        Account account = accountRepository.findByUsername(username);

        if (account == null || !account.getPassword().equals(password)) {
            throw new AuthenticationException("Invalid Credentials");
        }
        return account;
    }



}
