package com.greamz.backend.repository;

import com.greamz.backend.model.CodeActive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICodeActiveRepo extends JpaRepository<CodeActive, Long> {
    public List<CodeActive> findAllByGameAppid(Long appid);
}
