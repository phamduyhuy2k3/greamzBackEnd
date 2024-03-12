package com.greamz.backend;


import com.greamz.backend.dto.account.AccountRequest;
import com.greamz.backend.enumeration.Role;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.service.AccountModelService;
import com.greamz.backend.service.AccountService;
import com.greamz.backend.util.GlobalState;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories
@EnableCaching
@RequiredArgsConstructor
public class BackEndApplication {
    private final AccountModelService accountModelService;
    private final AccountService accountService;

    public static void main(String[] args) {

        SpringApplication.run(BackEndApplication.class, args);

    }
    @PostConstruct
    public void init() {
        AccountRequest accountRequest = AccountRequest
                .builder()
                .email("huyeptrai821@gmail.com")
                .username("phamduyhuy")
                .password("123456")
                .isEnabled(true)
                .role(Role.ADMIN)
                .fullname("Pham Duy Huy")
                .build();
        Optional<AccountModel> accountModel = accountService.findByUserNameOrEmail("huyeptrai821@gmail.com");
        if (accountModel.isEmpty()) {
            accountModelService.saveAccount(accountRequest);
        }
    }

}
