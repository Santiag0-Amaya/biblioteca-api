package com.santiago.biblioteca.service;

import java.time.LocalDate;
import java.util.List;

import com.santiago.biblioteca.model.Libro;
import com.santiago.biblioteca.model.Prestamo;
import com.santiago.biblioteca.model.Usuario;
import com.santiago.biblioteca.repository.ILibroRepository;
import com.santiago.biblioteca.repository.IPrestamoRepository;
import com.santiago.biblioteca.repository.IUserRepository;

public class PrestamoService implements IPrestamoService{

    private IPrestamoRepository prestamoRepository;
    private ILibroRepository libroRepository;
    private IUserRepository userRepository;
    public PrestamoService (IPrestamoRepository prestamoRepository, ILibroRepository libroRepository, IUserRepository userRepository) {
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<Prestamo> mostrarPrestamos() {
        return prestamoRepository.findAll();
    }

    @Override
    public Prestamo buscarId(int idPrestamo) {
        return prestamoRepository.findById(idPrestamo).orElse(null);
    }

    @Override
    public Prestamo agregarPrestamo(Prestamo prestamo) {
        if(prestamo == null) return null;

        int idUsuario = prestamo.getUsuario().getId();
        Usuario usuario = userRepository.findById(idUsuario).orElse(null);
        if(usuario == null) return null;

        int idLibro = prestamo.getLibro().getId();
        Libro libro = libroRepository.findById(idLibro).orElse(null);
        if(libro == null) return null;
        if(libro.getStockDisponible()<=0) return null;

        // pongo los libros y usuarios reales traidos de la bd. tenia solo sus id
        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);

        libro.setStockDisponible(libro.getStockDisponible()-1);

        // actualizo el stock
        libroRepository.save(libro);

        return prestamoRepository.save(prestamo);

    }

    @Override
    public boolean removerPrestamo(int idPrestamo) {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo).orElse(null);
        if(prestamo == null) return false;
        prestamoRepository.delete(prestamo);
        return true;
    }

    @Override
    public Prestamo editarPrestamo(Prestamo prestamo, int idPrestamo) {
        
        Prestamo prestamoExistente = buscarId(idPrestamo);

        if (prestamoExistente == null || prestamo == null) {
            return null;
        }
        prestamoExistente.setFechaDevolucionPrevista(prestamo.getFechaDevolucionPrevista());

        return prestamoRepository.save(prestamoExistente);
    }

    @Override
    public boolean registrarDevolucion(int idPrestamo) {

        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElse(null);

        if (prestamo == null) {
            return false;
        }

        if (prestamo.getEstado().equals("DEVUELTO")) {
            return false;
        }

        Libro libro = prestamo.getLibro();

        libro.setStockDisponible( libro.getStockDisponible() + 1);

        libroRepository.save(libro);

        prestamo.setEstado("DEVUELTO");
        prestamo.setFechaDevolucionReal(LocalDate.now());

        prestamoRepository.save(prestamo);

        return true;
    }



}
