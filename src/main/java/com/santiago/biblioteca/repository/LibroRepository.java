package com.santiago.biblioteca.repository;

import java.util.List;
import java.util.Optional; // Importamos Optional

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.sql.Statement;
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
    
    // Programamos el findById para que use Optional nativamente
    public Optional<Libro> findById(int id) {
        String sql = "SELECT * FROM libros WHERE id = ?";
        try {
            Libro libro = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Libro l = new Libro();
                l.setId(rs.getInt("id"));
                l.setTitulo(rs.getString("titulo"));
                l.setAutor(rs.getString("autor"));
                l.setIsbn(rs.getString("isbn"));
                l.setAnioPublicacion(rs.getInt("anio_publicacion"));
                l.setStockTotal(rs.getInt("stock_total"));
                l.setStockDisponible(rs.getInt("stock_disponible"));
                return l;
            }, id);
            return Optional.ofNullable(libro); // Si lo encuentra, mete el libro en la cajita
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // Si no lo encuentra, devolve la cajita vacía
        }
    }

    // Modificado para que inserte y te devuelva el Libro con su ID nuevo
    public Libro save(Libro libro) {
        String sql = "INSERT INTO libros (titulo, autor, isbn, anio_publicacion, stock_total, stock_disponible) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setInt(4, libro.getAnioPublicacion());
            ps.setInt(5, libro.getStockTotal());
            ps.setInt(6, libro.getStockDisponible());
            return ps;
        }, keyHolder);

        // Recuperamos el ID autoincremental que asignó la BD y se lo seteamos al objeto
        if (keyHolder.getKey() != null) {
            libro.setId(keyHolder.getKey().intValue());
        }
        return libro;
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