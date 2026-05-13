package com.santiago.biblioteca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.santiago.biblioteca.model.Usuario;
import com.santiago.biblioteca.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;

    public List<Usuario> getUsuarios() {
        return usuarioService.obtenerTodosLosUsuarios();
    }

    public Usuario getUsuario(@PathVariable int id) {
        return usuarioService.obtenerPorId(id);
    }

    @PostMapping
    public String crearUsuario(@RequestBody Usuario usuario){
        usuarioService.guardar(usuario);
        return "Creado con exito";
    }

    
}
