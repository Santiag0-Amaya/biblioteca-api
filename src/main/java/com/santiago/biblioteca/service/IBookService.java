package com.santiago.biblioteca.service;

import java.util.List;

import com.santiago.biblioteca.model.Book;

public interface IBookService {

    // READ
    List<Book> showBooks();
    Book findId(int idBook);
    boolean existBook (String isbn);
    
    // CREATE
    Book addBook(Book book);
    
    // DELETE
    boolean removeBook(int idBook);
    boolean logicallyDeleteBook(int idBook); // borrado logico
    
    //UPDATE
    Book editBook (int idBook, Book book);

    
}
