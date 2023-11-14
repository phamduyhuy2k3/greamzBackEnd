package com.greamz.backend.repository;

import com.greamz.backend.enumeration.Devices;

import com.greamz.backend.model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IPlatformRepo extends JpaRepository<Platform, Integer> {

    Set<Platform> findAllByDevices(Devices devices);


}
