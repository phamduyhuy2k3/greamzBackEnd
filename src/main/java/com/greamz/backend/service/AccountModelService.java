package com.greamz.backend.service;

import com.greamz.backend.model.AccountModel;
import com.greamz.backend.repository.IAccountRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Slf4j
public class AccountModelService {
    private final IAccountRepo repo;
    private final PasswordEncoder passwordEncoder;
    @Transactional
    public List<AccountModel> findAll(){
        List<AccountModel> accountModels = repo.findAll();
        for (AccountModel accountModel:accountModels
             ) {
            accountModel.setDisscusions(null);
            accountModel.setOrders(null);
            accountModel.setReviews(null);
            accountModel.setVouchers(null);

        }
        return accountModels;
    }

    @Transactional(readOnly = true)
    public AccountModel findAccountById(Integer id) throws NoSuchElementException{
        AccountModel accountModel = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + id));
        accountModel.setDisscusions(null);
        accountModel.setOrders(null);
        accountModel.setReviews(null);
        accountModel.setVouchers(null);
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + id));
    }

    @Transactional
    public AccountModel findById(Integer id) throws NoSuchElementException{
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + id));
    }

    @Transactional
    public AccountModel saveAccount(AccountModel account){
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return repo.save(account);
    }
    @Transactional
    public AccountModel updateAccount(AccountModel account){
        AccountModel accountModel = repo.findById(account.getId()).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + account.getId()));
        if(account.getPassword() == null || account.getPassword().isEmpty()){
            account.setPassword(accountModel.getPassword());
        }else {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
        }

        return repo.save(account);
    }
    @Transactional
    public void deleteAccountById(Integer id) {
        AccountModel accountModel = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + id));
        repo.deleteById(id);
    }
}
