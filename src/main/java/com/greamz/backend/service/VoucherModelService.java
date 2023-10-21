package com.greamz.backend.service;


import com.greamz.backend.model.Voucher;
import com.greamz.backend.repository.IVoucherRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Slf4j
public class VoucherModelService {
    private final IVoucherRepo voucherRepository;

    @Transactional
    public void saveVoucherModel(Voucher voucherModel) {
        voucherRepository.save(voucherModel);
    }

    @Transactional
    public List<Voucher> findAll() {
        return voucherRepository.findAll();
    }

    @Transactional
    public void deleteVoucherByAppid(Long id){
        Voucher voucherModel = voucherRepository.findById(id).orElseThrow(()->new NoSuchElementException("Not found product with id: "+ id));
        voucherRepository.deleteById(id);

    }

    @Transactional
    public Voucher findVoucherByid(Long id) throws NoSuchElementException {
        return voucherRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Not found product with id: " + id));
    }

    @Transactional
    public Voucher findByid(Long id){
        return voucherRepository.findById(id).orElseThrow(()->new NoSuchElementException("Not found product with id: "+ id));
    }

}
