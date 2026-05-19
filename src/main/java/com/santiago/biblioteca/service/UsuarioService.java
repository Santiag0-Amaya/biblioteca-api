package com.santiago.biblioteca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.santiago.biblioteca.model.Usuario;
import com.santiago.biblioteca.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    

    public List<Usuario> obtenerTodosLosUsuarios(){
        return usuarioRepository.findAll(); 
    }
    
    public Usuario obtenerPorId(int id) {
        return usuarioRepository.findId(id);
    }
    public void guardar(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public boolean puedePedirPrestamo(Usuario usuario){
        return usuario != null && usuario.getEstado().equals("VALIDO");
    }
}
