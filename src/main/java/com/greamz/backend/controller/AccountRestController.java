package com.greamz.backend.controller;

import com.greamz.backend.enumeration.CategoryTypes;
import com.greamz.backend.enumeration.Role;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.service.AccountModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AccountRestController {
    private final AccountModelService service;

    @GetMapping("/findAll")
    public ResponseEntity<Iterable<AccountModel>> findAll() {
        List<AccountModel> accountModels = service.findAll();
        return ResponseEntity.ok(accountModels);
    }
    @GetMapping("/authorities")
    public ResponseEntity<?> authorities(){
        return ResponseEntity.ok(Arrays.stream(Role.values()).map(Role::name).toList());
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<AccountModel> findById(@PathVariable("id") Integer id) {
        try {
          AccountModel accountModel = service.findAccountById(id);
          return ResponseEntity.ok(accountModel);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public AccountModel getOne(@PathVariable("id") Integer id) {
        return service.findById(id);
    }

    @PostMapping("/save")
    public AccountModel save(@RequestBody AccountModel account) {
        service.saveAccount(account);
        return account;
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        service.deleteAccountById(id);
    }
}
