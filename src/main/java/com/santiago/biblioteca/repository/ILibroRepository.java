package com.santiago.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.santiago.biblioteca.model.Libro;

@Repository
public interface ILibroRepository extends JpaRepository<Libro, Integer> {

    boolean existsByIsbn(String isbn);
}
