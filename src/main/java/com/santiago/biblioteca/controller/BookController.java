package com.santiago.biblioteca.controller;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.santiago.biblioteca.dto.BookRequestDto;
import com.santiago.biblioteca.model.Book;
import com.santiago.biblioteca.service.IBookService;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final IBookService bookService;

    public BookController (IBookService bookService) {
        this.bookService = bookService;
    }    

    private BookRequestDto toDto(Book book) {
        return new BookRequestDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublicationDate(),
                book.getTotalStock(),
                book.getAvailableStock()
        );
    }

    private Book toEntity(BookRequestDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setPublicationDate(dto.getPublicationDate());
        book.setTotalStock(dto.getTotalStock());
        book.setAvailableStock(dto.getAvailableStock());
        return book;
    }

    //READ
    @GetMapping
    public List<BookRequestDto> showBooks() {
        return bookService.showBooks()
        .stream()
        .map(this::toDto)
        .toList();
    }
    //READ PRODUCTO ESPECIFICO
    @GetMapping("/{id}")
    public ResponseEntity<?> findId (@PathVariable int id) {
        Book book = bookService.findId(id);
        if(book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("No se encuentra el Book con ese codigo");
        }
        return ResponseEntity.ok(toDto(book)); 
    } 
    
    @PostMapping
    public ResponseEntity<?> addBook (@RequestBody BookRequestDto bookDto) {
        if (bookDto == null || bookDto.getIsbn() == null || bookDto.getIsbn().isBlank()) {
            return ResponseEntity.badRequest().body("El ISBN del libro es obligatorio");
        }

        if (bookService.existBook(bookDto.getIsbn())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un libro con ese ISBN");
        }

        Book bookCreated;
        try {
            bookCreated = bookService.addBook(toEntity(bookDto));
        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un libro con ese ISBN");
        }

        if(bookCreated == null){
            return ResponseEntity.badRequest().body("Los datos del libro no son validos");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(bookCreated)); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editBook (@PathVariable int id, @RequestBody BookRequestDto bookDto) {
        if (bookDto == null || bookDto.getIsbn() == null || bookDto.getIsbn().isBlank()) {
            return ResponseEntity.badRequest().body("El ISBN del libro es obligatorio");
        }

        Book bookEdited;
        try {
            bookEdited = bookService.editBook(id, toEntity(bookDto));
        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un libro con ese ISBN");
        }
        if(bookEdited == null){
            return ResponseEntity.badRequest().body("No se pudo editar");
        }
        return ResponseEntity.ok(toDto(bookEdited));
    }

    @PutMapping("/{id}/logical-delete")
    public ResponseEntity<String> logicallyDeletedBook (@PathVariable int id) {
        boolean eliminatedBook = bookService.logicallyDeleteBook(id);

        if (!eliminatedBook) return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("No se encuentra el Book con ese codigo");

        return ResponseEntity.ok("Producto eliminado correctamente"); 

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removedBook (@PathVariable int id) {
        boolean eliminatedBook = bookService.removeBook(id);

        if (!eliminatedBook) return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("No se encuentra el Book con ese codigo");

        return ResponseEntity.ok("Producto eliminado correctamente"); 
    }

}
