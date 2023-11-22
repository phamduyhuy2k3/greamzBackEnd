package com.greamz.backend.controller;

import com.greamz.backend.dto.CodeActiveDTO;
import com.greamz.backend.model.CodeActive;
import com.greamz.backend.model.Review;
import com.greamz.backend.service.CodeActiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/codeActive")
@RequiredArgsConstructor
public class CodeActiveRestController {
    private final CodeActiveService codeActiveService;

    @GetMapping("/findAll")
    public List<CodeActiveDTO> findAll() {
        return codeActiveService.findAll();
    }
    @PostMapping("/create")
    public CodeActive create(@RequestBody CodeActive codeActive) {
        try {
            codeActiveService.save(codeActive);
            return codeActive;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/findByIdGame/{appid}")
    public List<CodeActiveDTO> findByIdGame(@PathVariable Long appid) {
        return codeActiveService.findByIdGame(appid);
    }
}
