package com.santiago.biblioteca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santiago.biblioteca.model.Libro;
import com.santiago.biblioteca.repository.LibroRepository;

@Service
public class LibroService {
    
    @Autowired
    private LibroRepository libroRepository;
    
    public List<Libro> obtenerTodosLosLibros() {
        // Aquí puedes agregar lógica antes de devolver
        // Por ejemplo: filtrar, ordenar, validar permisos, etc.
        return libroRepository.findAll();
    }

    public Libro obtenerPorId(int id) {
        return libroRepository.findId(id);
    }
    public void guardar(Libro libro) {
        libroRepository.save(libro);
    }


}
