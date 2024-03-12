package com.greamz.backend.repository;

import com.greamz.backend.model.AccountAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IAccountAuthProvider extends JpaRepository<AccountAuthProvider, Long> {
    List<AccountAuthProvider> findAllByAccount_Id(Integer id);
}
