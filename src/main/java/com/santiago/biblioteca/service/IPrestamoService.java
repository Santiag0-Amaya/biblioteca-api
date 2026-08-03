package com.santiago.biblioteca.service;

import java.util.List;

import com.santiago.biblioteca.model.Prestamo;

public interface IPrestamoService {

    // READ
    List<Prestamo> mostrarPrestamos();
    Prestamo buscarId(int idPrestamo);
    
    // CREATE
    Prestamo agregarPrestamo(Prestamo Prestamo);
    
    // DELETE
    boolean removerPrestamo(int idPrestamo);
    boolean registrarDevolucion(int idPrestamo);
    
    //UPDATE
    Prestamo editarPrestamo (Prestamo Prestamo, int idPrestamo);
}
