package com.example.repository;

import com.example.model.BankAccount;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BankAccountRepo extends CrudRepository<BankAccount, Integer> {
}
