package com.example.controllers;

import com.example.model.BankAccount;
import com.example.repository.BankAccountRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class BankController {
    @Resource
    private BankAccountRepo accountRepo;

    @GetMapping("/bank/accounts")
    public List<BankAccount> getAllAcounts(){
        return (List<BankAccount>) accountRepo.findAll();
    }
}