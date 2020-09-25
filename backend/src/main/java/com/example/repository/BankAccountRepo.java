package com.example.repository;

import com.example.model.BankAccount;

import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.CrudRepository;

@RepositoryRestResource
public interface BankAccountRepo extends CrudRepository<BankAccount, Integer> {
}
