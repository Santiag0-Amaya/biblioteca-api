package com.santiago.biblioteca.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.santiago.biblioteca.model.Libro;
import com.santiago.biblioteca.service.LibroService;

@RestController
@RequestMapping("/libros")
public class LibroController {
    
    @Autowired
    private LibroService libroService; 
    
    @GetMapping
    public ResponseEntity<List<Libro>> getLibros() {
        List<Libro> libros = libroService.obtenerTodosLosLibros();
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> getLibroId(@PathVariable int id) {
        Optional<Libro> libroOpcional = libroService.obtenerPorId(id);
        
        // Desempaquetamos la cajita Optional
        if (libroOpcional.isPresent()) {
            return ResponseEntity.ok(libroOpcional.get()); // 200 OK + JSON del libro
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found si está vacío
        }
    }

    @PostMapping 
    public ResponseEntity<Libro> crearLibro(@RequestBody Libro libro) {
        Libro libroCreado = libroService.guardar(libro);
        // Devolvemos el 201 Created junto con el objeto entero creado
        return new ResponseEntity<>(libroCreado, HttpStatus.CREATED);
    }
}