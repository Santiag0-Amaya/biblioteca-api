package com.santiago.biblioteca.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Prestamo {
    private int id;
    private int usuarioId;
    private int libroId;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionPrevista;
    private LocalDate fechaDevolucionReal;
    private String estado;

}
