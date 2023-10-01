package com.greamz.backend.repository;


import com.greamz.backend.model.AccountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IAccountRepo extends JpaRepository<AccountModel, Integer> {
    @Query("select a from AccountModel  a where a.username=?1 or a.email=?1")
    Optional<AccountModel> findByUserNameOrEmail(String username);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
