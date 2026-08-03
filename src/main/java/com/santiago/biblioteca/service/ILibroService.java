package com.santiago.biblioteca.service;

import java.util.List;

import com.santiago.biblioteca.model.Libro;

public interface ILibroService {

    // READ
    List<Libro> mostrarLibros();
    Libro buscarId(int idLibro);
    boolean existeLibro (String isbn);
    
    // CREATE
    Libro agregarLibro(Libro libro);
    
    // DELETE
    boolean removerLibro(int idLibro);
    
    //UPDATE
    Libro editarLibro (int idLibro, Libro libro);

    
}
