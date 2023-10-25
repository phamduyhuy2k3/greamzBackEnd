package com.greamz.backend.controller;

import com.greamz.backend.model.Countries;
import com.greamz.backend.service.CountriesModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/country")
@RequiredArgsConstructor
public class CountryRestController {
    private final CountriesModelService countriesModelService;
    @GetMapping("/all")
    public ResponseEntity<List<Countries>> findAll(){
        return ResponseEntity.ok(countriesModelService.findAll());
    }
}
