package com.greamz.backend.service;

import com.greamz.backend.model.Movie;
import com.greamz.backend.repository.IMovieRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MovieService {
    private final IMovieRepo movieRepo;
    @Transactional
    public void saveMovie(Movie movie) {
        movieRepo.save(movie);
    }
    @Transactional
    public void saveAllMovies(List<Movie> movies) {
        movieRepo.saveAll(movies);
    }

}
