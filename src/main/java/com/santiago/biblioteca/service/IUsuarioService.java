package com.santiago.biblioteca.service;

import java.util.List;

import com.santiago.biblioteca.model.Usuario;

public interface IUsuarioService {

    // READ
    List<Usuario> mostrarUsuarios();
    Usuario buscarId(int idUsuario);
    boolean existeUsuario (String dni);
    
    // CREATE
    Usuario agregarUsuario(Usuario Usuario);
    
    // DELETE
    boolean removerUsuario(int idUsuario);
    boolean borradoUsuario(int idUsuario); // logico
    
    //UPDATE
    Usuario editarUsuario (int idUsuario, Usuario Usuario);

}
