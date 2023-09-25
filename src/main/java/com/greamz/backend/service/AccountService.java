package com.greamz.backend.service;


import com.greamz.backend.model.AccountModel;
import com.greamz.backend.repository.IAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final IAccountRepository iAcountRepostitory;
    public Optional<AccountModel> findByUserNameOrEmail(String username) {
        return iAcountRepostitory.findByUserNameOrEmail(username);
    }
}
