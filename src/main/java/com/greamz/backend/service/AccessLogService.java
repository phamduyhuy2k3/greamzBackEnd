package com.greamz.backend.service;

import com.greamz.backend.model.AccessLogModal;
import com.greamz.backend.repository.IAccessLogRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class AccessLogService {
    @Autowired
    private IAccessLogRepo repo;

    public void saveAccessLog(String ipAddress) {
        AccessLogModal accessLog = new AccessLogModal();
        accessLog.setIpAddress(ipAddress);
        accessLog.setAccessTime(LocalDateTime.now());
        repo.save(accessLog);
    }
}
