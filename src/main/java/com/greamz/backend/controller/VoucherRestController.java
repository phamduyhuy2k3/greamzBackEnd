package com.greamz.backend.controller;

import com.greamz.backend.model.GameModel;
import com.greamz.backend.model.Voucher;
import com.greamz.backend.service.VoucherModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/voucher")
@RequiredArgsConstructor
public class VoucherRestController {
    private final VoucherModelService service;

    @GetMapping("/findALl")
    public ResponseEntity<Iterable<Voucher>> findAll(){
        List<Voucher> voucherModels = service.findAll();
        return ResponseEntity.ok(voucherModels);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Voucher> findByid(@PathVariable("id") Long id){
        try {
            Voucher voucherModels = service.findVoucherByid(id);
            return ResponseEntity.ok(voucherModels);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public Voucher create(@RequestBody Voucher voucher){
        service.saveVoucherModel(voucher);
        return voucher;
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id){
        service.deleteVoucherByAppid(id);
    }

    @GetMapping("{id}")
    public Voucher getOne(@PathVariable("id") Long appid) {
        return service.findByid(appid);
    }
}
