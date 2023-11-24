package com.greamz.backend.service;


import com.greamz.backend.dto.account.UserProfileDTO;
import com.greamz.backend.enumeration.AuthProvider;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.repository.IAccountRepo;
import com.greamz.backend.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IAccountRepo accountRepository;
    public UserProfileDTO getProfile(String username){
        AccountModel accountModel=accountRepository
                .findByUserNameOrEmail(username).orElseThrow(()->new UsernameNotFoundException("User not found"));

        return Mapper.mapObject(accountModel, UserProfileDTO.class);
    }
    public Boolean isUsernameExists(String username){
        return accountRepository.existsByUsername(username);
    }
    public Boolean isEmailExists(String email){
        return accountRepository.existsByEmail(email);
    }
    public Boolean isEmailExistsProviderLocal(String email){
        return accountRepository.existsByEmailAndProvider(email, AuthProvider.local);
    }
}
