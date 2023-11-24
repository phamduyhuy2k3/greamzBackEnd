package com.greamz.backend.repository;

import com.greamz.backend.model.Review;
import com.greamz.backend.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IVoucherRepo extends JpaRepository<Voucher,Long>{


}
