package com.santiago.biblioteca.model;

import lombok.Data;

@Data
public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private int anioPublicacion;
    private int stockTotal;        
    private int stockDisponible;   
 
}
