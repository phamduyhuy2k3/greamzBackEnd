package com.greamz.backend.service;

import com.greamz.backend.enumeration.Devices;
import com.greamz.backend.model.Platform;
import com.greamz.backend.repository.IPlatformRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class PlatformService {
    private final IPlatformRepo repo;

    @Transactional(readOnly = true)
    public List<Platform> findAll() {
        List<Platform> platforms = repo.findAll();
        platforms.forEach(platform -> Hibernate.initialize(platform.getDevices()));

        return platforms;
    }

    @Transactional
    public Platform findById(Integer id) throws NoSuchElementException {
        Platform platforms =repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found game platform with id: " + id));
        Hibernate.initialize(platforms.getDevices());
        return platforms;
    }

    @Transactional
    public Platform savePlatform(Platform platform) throws SQLIntegrityConstraintViolationException {
        return repo.save(platform);
    }

    @Transactional
    public void deletePlatform(Integer id) {
        repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found game platform with id: " + id));
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Platform> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Platform> platforms =repo.findAll(pageable);
        platforms.forEach(platform -> Hibernate.initialize(platform.getDevices()));
        return platforms;
    }

    public Set<Platform> findAllByDevices(Devices device) {
        return repo.findAllByDevices(device);
    }
}
