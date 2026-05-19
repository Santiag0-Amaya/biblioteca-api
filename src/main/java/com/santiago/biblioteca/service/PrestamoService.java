package com.santiago.biblioteca.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santiago.biblioteca.model.Libro;
import com.santiago.biblioteca.model.Prestamo;
import com.santiago.biblioteca.model.Usuario;
import com.santiago.biblioteca.repository.LibroRepository;
import com.santiago.biblioteca.repository.PrestamoRepository;

@Service
public class PrestamoService {
    
    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Prestamo registrarPrestamo(int usuarioID, int libroID){
        Usuario usuario = usuarioService.obtenerPorId(usuarioID);
        Libro libro = libroRepository.findId(libroID);

        if(usuario == null) throw new RuntimeException("Usuario inexistente");
        if(libro == null) throw new RuntimeException("Libro inexistente");

        if(!usuarioService.puedePedirPrestamo(usuario)) throw new RuntimeException("Este usuario no puede pedir prestamo");
        if(libro.getStockDisponible() < 1) throw new RuntimeException("No hay copias disponibles de este libro");

        Prestamo prestamo = new Prestamo();
        prestamo.setLibroId(libroID);
        prestamo.setUsuarioId(usuarioID);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucionPrevista(LocalDate.now().plusDays(14));
        prestamo.setEstado("ACTIVO");

        prestamoRepository.save(prestamo);
        libro.setStockDisponible(libro.getStockDisponible()-1);
        libroRepository.update(libro);

        return prestamo;

    }

    public Prestamo registrarDevolucion(int prestamoID){
        Prestamo prestamo = prestamoRepository.findId(prestamoID);
        if(prestamo == null) throw new RuntimeException("Prestamo inexistente");
        if(prestamo.getEstado().equals("DEVUELTO")) throw new RuntimeException("Prestamo devuelto");
        
        prestamo.setEstado("DEVUELTO");
        prestamo.setFechaDevolucionReal(LocalDate.now());
        prestamoRepository.update(prestamo);

        Libro libro = libroRepository.findId(prestamo.getLibroId());
        if (libro != null) {
            libro.setStockDisponible(libro.getStockDisponible()+1);
            libroRepository.update(libro);
        }

        return prestamo;

    }
    
    public List<Prestamo> obtenerTodosLosPrestamos() {
        // Aquí puedes agregar lógica antes de devolver
        // Por ejemplo: filtrar, ordenar, validar permisos, etc.
        return prestamoRepository.findAll();
    }

    public Prestamo obtenerPorId(int id) {
        return prestamoRepository.findId(id);
    }




}
