package com.santiago.biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.santiago.biblioteca.model.Book;
import com.santiago.biblioteca.repository.IBookRepository;
@Service
public class BookService implements IBookService {

    
    private IBookRepository bookRepository;

    public BookService (IBookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean existBook (String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

    @Override
    public List<Book> showBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book findId(int idBook) {
        return bookRepository.findById(idBook).orElse(null);
    }

    @Override
    public Book addBook(Book book) {
        if(book == null || existBook(book.getIsbn())) return null;

        // retorno asi, para que la id se genere en la bd y se devuelva junto al producto
        return bookRepository.save(book);
    }

    @Override
    public boolean removeBook(int idBook) {
        Book bookExisting = findId(idBook);
        if(bookExisting == null) return false;

        bookRepository.delete(bookExisting);
        return true;
    }

    @Override
    public boolean logicallyDeleteBook (int idBook) {
        Book bookExisting = findId(idBook);
        if(bookExisting == null) return false;

        bookExisting.setDeleted(true);
        bookRepository.save(bookExisting);
        return true;
    }

    @Override
    public Book editBook(int idBook, Book book) {
        Book bookExisting = findId(idBook);
        if(bookExisting == null || book == null) return null;

        bookExisting.setAuthor(book.getAuthor());
        bookExisting.setIsbn(book.getIsbn());
        bookExisting.setPublicationDate(book.getPublicationDate());
        bookExisting.setAvailableStock(book.getAvailableStock());
        bookExisting.setTotalStock(book.getTotalStock());
        bookExisting.setTitle(book.getTitle());

        return bookRepository.save(bookExisting);
    }

}
