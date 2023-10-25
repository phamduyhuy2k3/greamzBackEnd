package com.greamz.backend.repository;

import com.greamz.backend.model.Countries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICountries extends JpaRepository<Countries, Long> {

}
