package com.santiago.biblioteca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.santiago.biblioteca.model.Prestamo;
import com.santiago.biblioteca.service.PrestamoService;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {
    @Autowired
    private PrestamoService prestamoService; 
    
    @GetMapping
    public List<Prestamo> getPrestamos() {
        return prestamoService.obtenerTodosLosPrestamos();
    }

    @GetMapping("{id}")
    public Prestamo getPrestamoId(@PathVariable int id) {
        return prestamoService.obtenerPorId(id);
    }
    @PostMapping 
    public String crearPrestamo(@RequestBody Prestamo Prestamo) {
        prestamoService.guardar(Prestamo);
        return("Prestamo creado con exito");
    }
}
