package com.greamz.backend.controller;

import com.greamz.backend.enumeration.Devices;
import com.greamz.backend.model.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.greamz.backend.service.PlatformService;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/platform")
@RequiredArgsConstructor
public class PlatformRestController {
    private final PlatformService service;

    @GetMapping("/findAllByDevice/{device}")
    public ResponseEntity<Set<Platform>> findAllByDevice(@PathVariable Devices device) {
        return ResponseEntity.ok(service.findAllByPlatformTypes(device));
    }

    @GetMapping("/devices")
    public ResponseEntity<List<String>> findPlatformByDevice() {
        return ResponseEntity.ok(Arrays.stream(Devices.values()).map(Devices::name).toList());
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Platform>> findAll() {
        List<Platform> gamePlatforms = service.findAll();
        return ResponseEntity.ok(gamePlatforms);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Platform> findById(@PathVariable("id") Integer id) {
        try {
            Platform platform = service.findById(id);
            return ResponseEntity.ok(platform);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public Platform getOne(@PathVariable("id") Integer id) {
        return service.findById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        service.deletePlatform(id);
    }

    @PostMapping("/save")
    public Platform save(@RequestBody Platform platform) {
        service.savePlatform(platform);
        return platform;
    }

    @GetMapping("/findAllPagination")
    public ResponseEntity<?> findAllPagination(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.findAll(page, size));
    }
}
