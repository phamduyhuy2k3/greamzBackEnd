package com.greamz.backend.service;

import com.greamz.backend.dto.CodeActiveDTO;
import com.greamz.backend.dto.GameBasicDTO;
import com.greamz.backend.dto.PlatformBasicDTO;
import com.greamz.backend.model.CodeActive;
import com.greamz.backend.repository.ICodeActiveRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CodeActiveService {
    private final ICodeActiveRepo repo;

    @Transactional
    public CodeActive save(CodeActive codeActive) throws SQLIntegrityConstraintViolationException {
        CodeActive codeActiveSaved = repo.save(codeActive);
        return codeActiveSaved;
    }

    @Transactional(readOnly = true)
    public List<CodeActiveDTO> findAll() {
        List<CodeActive> codeActive = repo.findAll();
        List<CodeActiveDTO> codeActiveDTOList = codeActive.stream()
                .map(codeActive1 -> {
                    Hibernate.initialize(codeActive1.getGame());
                    CodeActiveDTO codeActiveDTO = new CodeActiveDTO();
                    codeActiveDTO.setGame(
                            GameBasicDTO // <--- This is the line that is causing the error
                                    .builder()
                                    .appid(codeActive1.getGame().getAppid())
                                    .name(codeActive1.getGame().getName())
                                    .build()
                    );
                    Hibernate.initialize(codeActive1.getPlatform());
                    codeActiveDTO.setPlatform(
                            PlatformBasicDTO // <--- This is the line that is causing the error
                                    .builder()
                                    .id(codeActive1.getPlatform().getId())
                                    .name(codeActive1.getPlatform().getName())
                                    .build()
                    );
                    codeActiveDTO.setActive(codeActive1.getActive());
                    codeActiveDTO.setCode(codeActive1.getCode());
                    codeActiveDTO.setId(codeActive1.getId());
                    return codeActiveDTO;
                }).toList();
        return codeActiveDTOList;
    }

    @Transactional(readOnly = true)
    public List<CodeActiveDTO> findByIdGame(Long appid) {
        List<CodeActive> codeActive = repo.findAllByGameAppid(appid);
        List<CodeActiveDTO> codeActiveDTOList2 = codeActive.stream()
                .map(codeActive1 -> {
                    Hibernate.initialize(codeActive1.getGame());
                    CodeActiveDTO codeActiveDTO = new CodeActiveDTO();
                    codeActiveDTO.setGame(
                            GameBasicDTO // <--- This is the line that is causing the error
                                    .builder()
                                    .appid(codeActive1.getGame().getAppid())
                                    .name(codeActive1.getGame().getName())
                                    .build()
                    );
                    Hibernate.initialize(codeActive1.getPlatform());
                    codeActiveDTO.setPlatform(
                            PlatformBasicDTO // <--- This is the line that is causing the error
                                    .builder()
                                    .id(codeActive1.getPlatform().getId())
                                    .name(codeActive1.getPlatform().getName())
                                    .build()
                    );
                    codeActiveDTO.setActive(codeActive1.getActive());
                    codeActiveDTO.setCode(codeActive1.getCode());
                    codeActiveDTO.setId(codeActive1.getId());
                    return codeActiveDTO;
                }).toList();
        return codeActiveDTOList2;

    }

}
