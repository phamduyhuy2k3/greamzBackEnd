package com.greamz.backend.repository;

import com.greamz.backend.model.AccessLogModal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccessLogRepo extends JpaRepository<AccessLogModal, Long> {

}
