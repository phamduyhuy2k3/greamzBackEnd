package com.greamz.backend.repository;

import com.greamz.backend.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVoucherRepo extends JpaRepository<Voucher,Long>{
}
