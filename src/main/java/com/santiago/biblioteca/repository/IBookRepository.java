package com.santiago.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.santiago.biblioteca.model.Book;

@Repository
public interface IBookRepository extends JpaRepository<Book, Integer> {

    boolean existsByIsbn(String isbn);
}
