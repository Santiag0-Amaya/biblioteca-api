package com.santiago.biblioteca.service;

import java.util.List;

import com.santiago.biblioteca.model.Libro;
import com.santiago.biblioteca.repository.ILibroRepository;

public class LibroService implements ILibroService {

    
    private ILibroRepository libroRepository;

    public LibroService (ILibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public boolean existeLibro (String isbn) {
        return libroRepository.existsByIsbn(isbn);
    }

    @Override
    public List<Libro> mostrarLibros() {
        return libroRepository.findAll();
    }

    @Override
    public Libro buscarId(int idLibro) {
        return libroRepository.findById(idLibro).orElse(null);
    }

    @Override
    public Libro agregarLibro(Libro libro) {
        if(libro == null || existeLibro(libro.getIsbn())) return null;

        // retorno asi, para que la id se genere en la bd y se devuelva junto al producto
        return libroRepository.save(libro);
    }

    @Override
    public boolean removerLibro(int idLibro) {
        Libro libroExistente = buscarId(idLibro);
        if(libroExistente == null) return false;

        libroRepository.delete(libroExistente);
        return true;
    }

    @Override
    public Libro editarLibro(int idLibro, Libro libro) {
        Libro libroExistente = buscarId(idLibro);
        if(libroExistente == null || libro == null) return null;

        libroExistente.setAutor(libro.getAutor());
        libroExistente.setIsbn(libro.getIsbn());
        libroExistente.setAnioPublicacion(libro.getAnioPublicacion());
        libroExistente.setStockDisponible(libro.getStockDisponible());
        libroExistente.setStockTotal(libro.getStockTotal());
        libroExistente.setTitulo(libro.getTitulo());

        return libroRepository.save(libroExistente);
    }

}
