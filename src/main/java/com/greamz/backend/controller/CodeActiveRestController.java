package com.greamz.backend.controller;

import com.greamz.backend.dto.code.CodeActiveBasicDTO;
import com.greamz.backend.dto.code.CodeActiveDTO;
import com.greamz.backend.model.CodeActive;
import com.greamz.backend.security.UserPrincipal;
import com.greamz.backend.service.CodeActiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> create(@RequestBody CodeActive codeActive) {
        Map<String,Object> errors = new HashMap<>();
        try {
            codeActiveService.save(codeActive);
            return ResponseEntity.ok(codeActive);
        } catch (DataIntegrityViolationException e) {
            errors.put("code", "Mã code đã tồn tại");
            return ResponseEntity.badRequest().body(errors);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Lỗi");
        }
    }

    @GetMapping("/findByIdGame/{appid}")
    public List<CodeActiveDTO> findByIdGame(@PathVariable Long appid) {
        return codeActiveService.findByIdGame(appid);
    }
    @GetMapping("/findAllByAccount_Id/{appid}")
    public ResponseEntity<Map<String,Object>> findAllByAccount_IdAndGame_id(@PathVariable("appid") Long appid, @AuthenticationPrincipal UserPrincipal account) {
        return ResponseEntity.ok(codeActiveService.findByAccountId(account.getId(),appid));
    }
}
