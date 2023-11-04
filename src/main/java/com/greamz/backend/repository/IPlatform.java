package com.greamz.backend.repository;

import com.greamz.backend.enumeration.Devices;
import com.greamz.backend.model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface IPlatform extends JpaRepository<Platform,Integer> {
    Set<Platform> findAllByDevices(Devices devices);
}
