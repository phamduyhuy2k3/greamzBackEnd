package com.greamz.backend.service;

import com.greamz.backend.model.Countries;
import com.greamz.backend.model.GameCategory;
import com.greamz.backend.repository.ICountries;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CountriesModelService {
    private final ICountries countriesRepository;

    @Transactional
    public List<Countries> findAll() {
        return countriesRepository.findAll();
    }

}
