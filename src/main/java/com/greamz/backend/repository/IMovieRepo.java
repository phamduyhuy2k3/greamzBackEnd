package com.greamz.backend.repository;

import com.greamz.backend.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMovieRepo extends JpaRepository<Movie, String> {
}
