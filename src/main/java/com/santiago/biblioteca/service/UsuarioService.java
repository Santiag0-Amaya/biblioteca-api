package com.santiago.biblioteca.service;

import java.util.List;

import com.santiago.biblioteca.model.Usuario;
import com.santiago.biblioteca.repository.IUserRepository;

public class UsuarioService implements IUsuarioService {

    private IUserRepository userRepository;

    @Override
    public boolean existeUsuario (String dni) {
        return userRepository.existsByDni(dni);
    }

    public UsuarioService (IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<Usuario> mostrarUsuarios() {
        return userRepository.findAll();
    }

    @Override
    public Usuario buscarId(int idUsuario) {
        return userRepository.findById(idUsuario).orElse(null);
    }

    @Override
    public Usuario agregarUsuario(Usuario usuario) {
        if(usuario == null || existeUsuario(usuario.getDni()) ) return null;

        return userRepository.save(usuario);

    }

    @Override
    public boolean removerUsuario(int idUsuario) {
        Usuario usuarioExistente = buscarId(idUsuario);
        if(usuarioExistente == null) return false;
        userRepository.delete(usuarioExistente);
        return true;
    }

    @Override
    public boolean borradoUsuario(int idUsuario) {
        Usuario usuarioExistente = buscarId(idUsuario);
        if(usuarioExistente == null) return false;
        usuarioExistente.setEliminado(true);
        userRepository.save(usuarioExistente);
        return true;
    }

    @Override
    public Usuario editarUsuario(int idUsuario, Usuario usuario) {
        Usuario usuarioExistente = buscarId(idUsuario);
        if (usuarioExistente == null || usuario == null) return null;
        if (!usuarioExistente.getDni().equals(usuario.getDni()) && existeUsuario(usuario.getDni())) return null;

        usuarioExistente.setDni(usuario.getDni()); 
        usuarioExistente.setNombre(usuario.getNombre()); 
        usuarioExistente.setApellido(usuario.getApellido());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setTelefono(usuario.getTelefono());

        return userRepository.save(usuarioExistente);
    }

}
