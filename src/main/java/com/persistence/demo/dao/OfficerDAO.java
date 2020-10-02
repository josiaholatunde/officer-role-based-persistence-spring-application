package com.persistence.demo.dao;

import com.persistence.demo.entities.Officer;

import java.util.List;
import java.util.Optional;

public interface OfficerDAO {
    long count();

    Officer save(Officer officer);

    List<Officer> findAll();

    Optional<Officer> findById(Integer id);

    void delete(Officer officer);

    boolean existsById(Integer id);
}
