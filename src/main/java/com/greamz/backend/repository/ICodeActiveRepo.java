package com.greamz.backend.repository;

import com.greamz.backend.dto.platform.PlatformDTO;
import com.greamz.backend.model.CodeActive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICodeActiveRepo extends JpaRepository<CodeActive, Long> {
    List<CodeActive> findAllByGameAppid(Long appid);

    @Query("""
                        SELECT new com.greamz.backend.dto.platform.PlatformDTO(
                         p.id,
                          p.name,
                          count(c.code)                            
                        )
                        FROM CodeActive c
                        left join c.platform p on p.id = c.platform.id
                        WHERE c.game.appid = ?1
                        group by p.id
                      
            """)
    List<PlatformDTO> findPlatformByGameAppid(Long appid);
    List<CodeActive> findAllByGameAppidAndPlatformId(Long appid, Integer platformId);
}
