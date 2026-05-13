package com.santiago.biblioteca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Libro> getLibros() {
        return libroService.obtenerTodosLosLibros();
    }

    @GetMapping("{id}")
    public Libro getLibroId(@PathVariable int id) {
        return libroService.obtenerPorId(id);
    }
    @PostMapping 
    public String crearLibro(@RequestBody Libro libro) {
        libroService.guardar(libro);
        return("libro creado con exito");
    }
}
