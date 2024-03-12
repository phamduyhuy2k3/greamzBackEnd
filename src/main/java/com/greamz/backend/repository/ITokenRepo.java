package com.greamz.backend.repository;


import com.greamz.backend.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ITokenRepo extends JpaRepository<Token, Integer> {

    @Query(value = """
            select t from Token t inner join AccountModel u\s
            on t.user.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(Integer id);


    Optional<Token> findByToken(String token);

    @Query(value = """
            select t from Token t where t.user.id = :id and (t.expired = false or t.revoked = false) and  t.tokenType = 'BEARER' and t.token = :token

                      """)
    Optional<Token> findByUser_IdAndToken(Integer id, String token);

    Optional<Token> findByTokenAndAndExpiredIsFalseAndRevokedIsFalse(String token);

    @Modifying
    @Query(value = """
            delete from Token t where t.user.id = :id and (t.expired = :expired or t.revoked = :revoked)
                      """)
    void deleteAllByUser_IdAndExpiredOrRevoked(Integer id, boolean expired, boolean revoked);
}
