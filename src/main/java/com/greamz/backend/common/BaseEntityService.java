package com.greamz.backend.common;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class BaseEntityService<T, ID, R extends JpaRepository<T, ID>> {
    protected final R repository;

    public BaseEntityService(R repository) {
        this.repository = repository;
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public Optional<T> getById(ID id) {
        return repository.findById(id);
    }

    public T save(T entity) {
        return repository.save(entity);
    }
    public List<T> saveAll(List<T> entities) {
        return repository.saveAll(entities);
    }

    public void delete(T entity) {
        repository.delete(entity);
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }
    public boolean any() { return repository.findAll().stream().count() > 0; }
}
