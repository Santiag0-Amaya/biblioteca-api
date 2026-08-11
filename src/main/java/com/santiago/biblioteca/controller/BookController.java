package com.santiago.biblioteca.controller;

import java.util.List;

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

import com.santiago.biblioteca.model.Book;
import com.santiago.biblioteca.service.IBookService;

@RestController
@RequestMapping("/api/Books")
public class BookController {
    private final IBookService bookService;

    public BookController (IBookService bookService) {
        this.bookService = bookService;
    }    

    //READ
    @GetMapping
    public List<Book> showBooks() {
        return bookService.showBooks();
    }
    //READ PRODUCTO ESPECIFICO
    @GetMapping("/{id}")
    public ResponseEntity<?> findId (@PathVariable int id) {
        Book book = bookService.findId(id);
        if(book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("No se encuentra el Book con ese codigo");
        }
        return ResponseEntity.ok(book); 
    } 

    @PostMapping
    public ResponseEntity<?> addBook (@RequestBody Book book) {
        Book bookCreated = bookService.addBook(book);
        if(bookCreated == null){
            return ResponseEntity.badRequest().body("Los datos de Book no son validos");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(bookCreated); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editBook (@PathVariable int id, @RequestBody Book book) {
        Book bookEdited = bookService.editBook(id, book);
        if(bookEdited == null){
            return ResponseEntity.badRequest().body("No se pudo editar");
        }
        return ResponseEntity.ok(bookEdited);
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
