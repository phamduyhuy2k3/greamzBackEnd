package com.greamz.backend.service;

import com.greamz.backend.enumeration.Role;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.model.Authority;
import com.greamz.backend.repository.IAuthorityRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthorityService {
    private final IAuthorityRepo authorityRepo;
    @Transactional
    public void deleteAuthorityByUserIdAndRole(Integer userId, String role){

        authorityRepo.deleteByAccount_IdAndRole(userId, role);
    }
    public void saveAuthority(Integer userId, Role role){
        AccountModel account = AccountModel.builder().id(userId).build();
        Authority authority = Authority.builder().account(account).role(role).build();
        authorityRepo.save(authority);
    }
    public Authority findByAccount_IdAndRole(Integer userId, String role){
        return authorityRepo.findByAccount_IdAndRole(userId, role);
    }
}
