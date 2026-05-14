package com.santiago.biblioteca.model;

import lombok.Data;

@Data
public class Usuario {
    private int id;
    private String dni;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String estado;  // VALIDO-INVALIDO

}
