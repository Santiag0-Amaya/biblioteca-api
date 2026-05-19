package com.santiago.biblioteca.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.santiago.biblioteca.model.Libro;

@Repository
public class LibroRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<Libro> findAll() {
        String sql = "SELECT * FROM libros";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Libro libro = new Libro();
            libro.setId(rs.getInt("id"));
            libro.setTitulo(rs.getString("titulo"));
            libro.setAutor(rs.getString("autor"));
            libro.setIsbn(rs.getString("isbn"));
            libro.setAnioPublicacion(rs.getInt("anio_publicacion"));
            libro.setStockTotal(rs.getInt("stock_total"));
            libro.setStockDisponible(rs.getInt("stock_disponible"));
            return libro;
        });
    }
    
    public Libro findId(int id) {
        String sql = "SELECT * FROM libros WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Libro libro = new Libro();
                libro.setId(rs.getInt("id"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setIsbn(rs.getString("isbn"));
                libro.setAnioPublicacion(rs.getInt("anio_publicacion"));
                libro.setStockTotal(rs.getInt("stock_total"));
                libro.setStockDisponible(rs.getInt("stock_disponible"));
                return libro;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void save(Libro libro) {
        String sql = "INSERT INTO libros (titulo, autor, isbn, anio_publicacion, stock_total, stock_disponible) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            libro.getTitulo(),
            libro.getAutor(), 
            libro.getIsbn(),
            libro.getAnioPublicacion(),
            libro.getStockTotal(),
            libro.getStockDisponible()
        );
    }

    public void update(Libro libro) {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, isbn = ?, anio_publicacion = ?, stock_total = ?, stock_disponible = ? WHERE id = ?";
        jdbcTemplate.update(sql,
            libro.getTitulo(),
            libro.getAutor(),
            libro.getIsbn(),
            libro.getAnioPublicacion(),
            libro.getStockTotal(),
            libro.getStockDisponible(),
            libro.getId()
        );
    }
}