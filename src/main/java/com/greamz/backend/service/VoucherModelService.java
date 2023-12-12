package com.greamz.backend.service;


import com.greamz.backend.model.GameModel;
import com.greamz.backend.model.Review;
import com.greamz.backend.model.Voucher;
import com.greamz.backend.repository.IVoucherRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Slf4j
public class VoucherModelService {
    private final IVoucherRepo voucherRepository;


    public Voucher saveVoucherModel(Voucher voucherModel) {
        return voucherRepository.saveAndFlush(voucherModel);
    }

    @Transactional
    public void updateVoucherModel(Voucher voucherModel) {
        Voucher voucher = voucherRepository.findById(voucherModel.getId()).orElseThrow();
        voucherRepository.save(voucherModel);
    }

    @Transactional
    public List<Voucher> findAll() {
        return voucherRepository.findAll();
    }

    @Transactional
    public Page<Voucher> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return voucherRepository.findAll(pageable);
    }

    @Transactional
    public void deleteVoucherByAppid(Long id) {
        Voucher voucherModel = voucherRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Not found product with id: " + id));
        voucherRepository.deleteById(id);

    }

    @Transactional
    public Voucher findVoucherByid(Long id) throws NoSuchElementException {
        return voucherRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Not found product with id: " + id));
    }

    @Transactional
    public Voucher findByid(Long id) {
        return voucherRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Not found product with id: " + id));
    }


}
