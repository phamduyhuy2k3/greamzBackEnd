package com.greamz.backend.service;

import com.greamz.backend.dto.account.*;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.repository.IAccountRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;

import static com.greamz.backend.util.Mapper.mapObject;

@Service
@AllArgsConstructor
@Slf4j
public class AccountModelService {
    private final IAccountRepo repo;
    private final PasswordEncoder passwordEncoder;
    @Transactional(readOnly = true)
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
        return accountModel;
    }

    @Transactional
    public AccountModel findById(Integer id) throws NoSuchElementException{
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + id));
    }

    @Transactional()
    public AccountModel saveAccount(AccountRequest account){
        System.out.println(account.getUsername());
        System.out.println(account.getPassword());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        AccountModel accountModel = mapObject(account, AccountModel.class);
        return repo.save(accountModel);
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
    public UserProfileDTO editUsername(EditUsername editUsername){
        AccountModel accountModel = repo.findById(editUsername.getId()).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + editUsername.getId()));
        accountModel.setUsername(editUsername.getUsername());
        AccountModel accountModel1 = repo.saveAndFlush(accountModel);
        return mapObject(accountModel1, UserProfileDTO.class);

    }
    @Transactional
    public UserProfileDTO editFullname(EditFullname editEmail){
        AccountModel accountModel = repo.findById(editEmail.getId()).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + editEmail.getId()));
        accountModel.setFullname(editEmail.getFullname());
        AccountModel accountModel1 = repo.saveAndFlush(accountModel);
        return mapObject(accountModel1, UserProfileDTO.class);

    }
    @Transactional
    public UserProfileDTO editPhoto(EditPhoto editPhoto){
        AccountModel accountModel = repo.findById(editPhoto.getId()).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + editPhoto.getId()));
        accountModel.setPhoto(editPhoto.getPhoto());
        AccountModel accountModel1 = repo.saveAndFlush(accountModel);
        return mapObject(accountModel1, UserProfileDTO.class);

    }
    @Transactional
    public UserProfileDTO userEditProfileBasic(UserProfileBasic userProfileBasic){
        AccountModel accountModel = repo.findById(userProfileBasic.getId()).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + userProfileBasic.getId()));
        accountModel.setUsername(userProfileBasic.getUsername());
        accountModel.setFullname(userProfileBasic.getFullname());
        if(userProfileBasic.getPhoto() != null || userProfileBasic.getPhoto().isEmpty()) {
            accountModel.setPhoto(userProfileBasic.getPhoto());

        }
        AccountModel accountModel1 = repo.saveAndFlush(accountModel);
        return mapObject(accountModel1, UserProfileDTO.class);

    }
    @Transactional
    public UserProfileDTO userEditProfileImportant(UserProfileImportant userProfileImportant){
        AccountModel accountModel = repo.findById(userProfileImportant.getId()).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + userProfileImportant.getId()));
        accountModel.setEmail(userProfileImportant.getEmail());
        accountModel.setPassword(passwordEncoder.encode(userProfileImportant.getPassword()));
        AccountModel accountModel1 = repo.saveAndFlush(accountModel);
        return mapObject(accountModel1, UserProfileDTO.class);

    }
    @Transactional
    public void deleteAccountById(Integer id) {
        AccountModel accountModel = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + id));
        repo.deleteById(id);
    }
    public static boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            return true;
        } catch (Exception e) {

            return false;
        }
    }
}
