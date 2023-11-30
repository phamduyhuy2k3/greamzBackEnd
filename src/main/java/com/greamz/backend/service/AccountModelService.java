package com.greamz.backend.service;

import com.greamz.backend.dto.account.*;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.model.Orders;
import com.greamz.backend.model.Voucher;
import com.greamz.backend.repository.IAccountRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

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


    @Transactional
    public void updateEnabledField(Integer userId, boolean enabled) {
        repo.updateEnabled(userId, enabled);
    }

    @Transactional(readOnly = true)
    public List<AccountModel> findAll() {
        List<AccountModel> accountModels = repo.findAll();
        for (AccountModel accountModel : accountModels) {
            accountModel.setOrders(null);
            accountModel.setReviews(null);
            accountModel.setVouchers(null);
        }
        return accountModels;
    }

    @Transactional(readOnly = true)
    public AccountModel findAccountById(Integer id) throws NoSuchElementException {
        AccountModel accountModel = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + id));

        accountModel.setOrders(null);
        accountModel.setReviews(null);
        accountModel.setVouchers(null);
        return accountModel;
    }

    @Transactional
    public AccountBasicDTO findById(Integer id) throws NoSuchElementException {
        AccountModel account123 = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + id));
        AccountBasicDTO accountBasicList = new AccountBasicDTO();
        accountBasicList.setId(account123.getId());
        accountBasicList.setUsername(account123.getUsername());
        accountBasicList.setEmail(account123.getEmail());
        accountBasicList.setFullname(account123.getFullname());
        accountBasicList.setPhoto(account123.getPhoto());
        accountBasicList.setRole(account123.getRole());

        return accountBasicList;
    }
//    @Transactional(readOnly = true)
//    public List<Orders> findAllOrdersByAccountID(Integer id) throws NoSuchElementException {
//        AccountModel accountModel = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + id));
//        List<Orders> orders = accountModel.getOrders();
//        return orders;
//    }

    @Transactional()
    public AccountModel saveAccount(AccountRequest account) {
        System.out.println(account.getUsername());
        System.out.println(account.getPassword());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        AccountModel accountModel = mapObject(account, AccountModel.class);
        return repo.save(accountModel);
    }

    @Transactional
    public AccountModel updateAccount(@Validated AccountRequest account) {
        AccountModel accountModel = repo.findById(account.getId()).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + account.getId()));
        accountModel.setUsername(account.getUsername());
        accountModel.setEmail(account.getEmail());
        accountModel.setFullname(account.getFullname());
        if (account.getPassword() != null || !account.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
        }else{

        }

        return repo.save(accountModel);
    }

    @Transactional
    public UserProfileDTO editUsername(EditUsername editUsername) {
        AccountModel accountModel = repo.findById(editUsername.getId()).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + editUsername.getId()));
        accountModel.setUsername(editUsername.getUsername());
        AccountModel accountModel1 = repo.saveAndFlush(accountModel);
        return mapObject(accountModel1, UserProfileDTO.class);

    }

    @Transactional
    public UserProfileDTO editFullname(EditFullname editEmail) {
        AccountModel accountModel = repo.findById(editEmail.getId()).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + editEmail.getId()));
        accountModel.setFullname(editEmail.getFullname());
        AccountModel accountModel1 = repo.saveAndFlush(accountModel);
        return mapObject(accountModel1, UserProfileDTO.class);

    }

    @Transactional
    public UserProfileDTO editPhoto(EditPhoto editPhoto) {
        AccountModel accountModel = repo.findById(editPhoto.getId()).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + editPhoto.getId()));
        accountModel.setPhoto(editPhoto.getPhoto());
        AccountModel accountModel1 = repo.saveAndFlush(accountModel);
        return mapObject(accountModel1, UserProfileDTO.class);

    }

    @Transactional
    public UserProfileDTO userEditProfileBasic(UserProfileBasic userProfileBasic) {
        AccountModel accountModel = repo.findById(userProfileBasic.getId()).orElseThrow(() -> new NoSuchElementException("Not found account with id: " + userProfileBasic.getId()));
        accountModel.setUsername(userProfileBasic.getUsername());
        accountModel.setFullname(userProfileBasic.getFullname());
        if (userProfileBasic.getPhoto() != null || userProfileBasic.getPhoto().isEmpty()) {
            accountModel.setPhoto(userProfileBasic.getPhoto());

        }
        AccountModel accountModel1 = repo.saveAndFlush(accountModel);
        return mapObject(accountModel1, UserProfileDTO.class);

    }

    @Transactional
    public UserProfileDTO userEditProfileImportant(UserProfileImportant userProfileImportant) {
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

    @Transactional(readOnly = true)
    public List<Voucher> findAllVouchersByAccountId(Integer accountId) {
        List<Voucher> vouchers = repo.findVoucherById(accountId);
        vouchers.forEach(reviews1 -> {

        });
        return vouchers;
    }

}
