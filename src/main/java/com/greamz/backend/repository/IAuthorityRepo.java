package com.greamz.backend.repository;

import com.greamz.backend.enumeration.Role;
import com.greamz.backend.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IAuthorityRepo extends JpaRepository<Authority, Integer> {
    @Query(value = "select * from authority a where a.account_id = ?1 and a.role =?2 ", nativeQuery = true)
    Authority findByAccount_IdAndRole(Integer userId, String role);
    @Modifying
    @Query(value = "delete from authority a where a.account_id = :userId and a.role =:role ", nativeQuery = true)
    void deleteByAccount_IdAndRole(@Param("userId")Integer userId,@Param("role") String role);
}
