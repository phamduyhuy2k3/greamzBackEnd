package com.greamz.backend.repository;

import com.greamz.backend.dto.code.CodeActiveDTO;
import com.greamz.backend.dto.game.GameLibrary;
import com.greamz.backend.dto.platform.PlatformDTO;
import com.greamz.backend.model.CodeActive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                        WHERE c.game.appid = ?1 and c.account is null and c.active = false
                        group by p.id
                      
            """)
    List<PlatformDTO> findPlatformByGameAppid(Long appid);

    int countByPlatform_IdAndGame_AppidAndAccountIsNullAndActiveIsFalse(Integer platformId, Long appid);
    @Query("""
                        SELECT new com.greamz.backend.dto.game.GameLibrary(
                         c.game.appid,
                          count(c.code),
                          c.game.name,
                          c.game.header_image
                        )
                        FROM CodeActive c
                        left join c.account a on a.id = c.account.id
                        left join c.game g on g.appid = c.game.appid
                        WHERE c.account.id = ?1
                        group by c.game.appid
                      
            """)
    Page<GameLibrary> findGameLibraryByAccountId(Integer accountId, Pageable pageable);
    List<CodeActive> findAllByAccount_IdAndGame_Appid(Integer accountId,Long appid);
    @Query("""
                        SELECT c
                        FROM CodeActive c
                        WHERE c.game.appid = ?1 and c.platform.id = ?2 and c.account is null and c.active = false
            """)
    List<CodeActive> findAllByGameAppidAndPlatformIdAndAccountIsNullAndIsActiveFalse(Long appid, Integer platformId);
}
