package com.greamz.backend.repository;

import com.greamz.backend.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IImageRepository extends JpaRepository<Image, String> {
    public List<Image> findByGameModel_Appid(Integer id);
}

