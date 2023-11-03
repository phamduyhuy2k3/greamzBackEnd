package com.greamz.backend.service;

import com.greamz.backend.enumeration.Devices;
import com.greamz.backend.model.Platform;
import com.greamz.backend.repository.IPlatform;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class PlatformService {
    private final IPlatform repo;
    @Transactional
    public List<Platform> findAll() {
        return repo.findAll();
    }

    @Transactional
    public Platform findById(Integer id) throws NoSuchElementException {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found game platform with id: " + id));
    }

    @Transactional
    public Platform savePlatform(Platform platform) {
        return repo.save(platform);
    }

    @Transactional
    public void deletePlatform(Integer id) {
        repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found game platform with id: " + id));
        repo.deleteById(id);
    }

    @Transactional
    public Page<Platform> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findAll(pageable);
    }

    public Set<Platform> findAllByDevices(Devices device) {
        return repo.findAllByDevices(device);
    }
}
