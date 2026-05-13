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
            Usuario Usuario = new Usuario();
            Usuario.setId(rs.getInt("id"));
            Usuario.setNombre(rs.getString("nombre"));
            Usuario.setApellido(rs.getString("apellido"));
            Usuario.setEmail(rs.getString("email"));
            Usuario.setTelefono(rs.getString("telefono"));
            return Usuario;
        });
    }
    
    public Usuario findId(int id) {
        String sql = "SELECT * FROM Usuarios WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Usuario Usuario = new Usuario();
            Usuario.setId(rs.getInt("id"));
            Usuario.setNombre(rs.getString("nombre"));
            Usuario.setApellido(rs.getString("apellido"));
            Usuario.setEmail(rs.getString("email"));
            Usuario.setTelefono(rs.getString("telefono"));
            return Usuario;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void save(Usuario Usuario) {
        String sql = "INSERT INTO Usuarios (nombre, apellido, email, telefono) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            Usuario.getNombre(),
            Usuario.getApellido(), 
            Usuario.getTelefono(),
            Usuario.getEmail()
        );
    }
}