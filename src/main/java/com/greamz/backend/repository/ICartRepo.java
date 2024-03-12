package com.greamz.backend.repository;


import com.greamz.backend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface ICartRepo extends JpaRepository<Cart, Long>{
    List<Cart> findAllByAccount_Id(Integer id);
    void deleteAllByAccount_Id(Integer id);
    Optional<Cart> findByAccount_IdAndGame_Appid(Integer accountId, Long gameId);
    Optional<Cart> findByGame_AppidAndPlatform_IdAndAccount_Id(Long gameId, Integer platformId, Integer accountId);
    Optional<Cart> findByIdAndAccount_Id(Long id, Integer accountId);
}
