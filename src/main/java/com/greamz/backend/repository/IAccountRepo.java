package com.greamz.backend.repository;


import com.greamz.backend.enumeration.AuthProvider;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IAccountRepo extends JpaRepository<AccountModel, Integer> {

    @Query("select a from AccountModel  a where a.username=?1 or a.email=?1 and a.provider=?2")
    Optional<AccountModel> findByUserNameOrEmailAndProvider(String username,AuthProvider provider);
    @Query("select a from AccountModel  a where a.username=?1 or a.email=?1")
    Optional<AccountModel> findByUserNameOrEmail(String username);
    Optional<AccountModel> findByEmailAndProvider(String email, AuthProvider provider);
    Optional<AccountModel> findByUsername(String username);
    @Query("select a.vouchers from AccountModel a where a.id=?1")
    List<Voucher> findVoucherById(Integer id);

    Boolean existsByEmail(String email);
    Boolean existsByEmailAndProvider(String email, AuthProvider provider);
    Boolean existsByUsername(String username);
}
