package com.greamz.backend.service;

import com.greamz.backend.model.AccountModel;
import com.greamz.backend.repository.IAccountRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Slf4j
public class AccountModelService {
    private final IAccountRepo repo;

    @Transactional
    public List<AccountModel> findAll(){
        return repo.findAll();
    }

    @Transactional
    public AccountModel findAccountById(Integer id) throws NoSuchElementException{
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + id));
    }

    @Transactional
    public AccountModel findById(Integer id) throws NoSuchElementException{
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + id));
    }

    @Transactional
    public AccountModel saveAccount(AccountModel account){
        return repo.save(account);
    }

    @Transactional
    public void deleteAccountById(Integer id) {
        AccountModel accountModel = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + id));
        repo.deleteById(id);
    }
}
