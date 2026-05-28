package com.santiago.biblioteca.service;

import java.util.List;
import java.util.Optional; // 1. Importamos Optional

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santiago.biblioteca.model.Libro;
import com.santiago.biblioteca.repository.LibroRepository;

@Service
public class LibroService {
    
    @Autowired
    private LibroRepository libroRepository;
    
    public List<Libro> obtenerTodosLosLibros() {
        return libroRepository.findAll();
    }

    // 2. Cambiamos el tipo de retorno de 'Libro' a 'Optional<Libro>'
    public Optional<Libro> obtenerPorId(int id) {
        // 3. Usamos 'findById' que es el método nativo de Spring Data JPA
        return libroRepository.findById(id); 
    }

    // 4. Modificamos el guardar para que devuelva el libro creado
    public Libro guardar(Libro libro) {
        return libroRepository.save(libro); // save() ya devuelve la entidad persistida con su ID
    }
}