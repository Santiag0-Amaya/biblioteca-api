package com.santiago.biblioteca.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.santiago.biblioteca.model.Usuario;

@Repository
public class UsuarioRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<Usuario> findAll() {
        String sql = "SELECT * FROM Usuarios";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getInt("id"));
            usuario.setDni(rs.getString("dni"));
            usuario.setNombre(rs.getString("nombre"));
            usuario.setApellido(rs.getString("apellido"));
            usuario.setEmail(rs.getString("email"));
            usuario.setTelefono(rs.getString("telefono"));
            usuario.setEstado(rs.getString("estado"));
            return usuario;
        });
    }
    
    public Usuario findId(int id) {
        String sql = "SELECT * FROM Usuarios WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getInt("id"));
            usuario.setDni(rs.getString("dni"));
            usuario.setNombre(rs.getString("nombre"));
            usuario.setApellido(rs.getString("apellido"));
            usuario.setEmail(rs.getString("email"));
            usuario.setTelefono(rs.getString("telefono"));
            usuario.setEstado(rs.getString("estado"));
            return usuario;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void save(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (dni, nombre, apellido, email, telefono, estado) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            usuario.getDni(),
            usuario.getNombre(),
            usuario.getApellido(), 
            usuario.getEmail(),
            usuario.getTelefono(),
            usuario.getEstado()
        );
    }

    public void update(Usuario usuario) {
        String sql = "UPDATE usuarios SET email = ?, telefono = ?, estado = ? WHERE id = ?";
        jdbcTemplate.update(sql,
            usuario.getEmail(),
            usuario.getTelefono(),
            usuario.getEstado(),
            usuario.getId()
        );
    }
}