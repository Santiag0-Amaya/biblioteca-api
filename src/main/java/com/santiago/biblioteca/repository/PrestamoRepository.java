package com.santiago.biblioteca.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.santiago.biblioteca.model.Prestamo;


@Repository
public class PrestamoRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<Prestamo> findAll() {
        String sql = "SELECT * FROM prestamos";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Prestamo prestamo = new Prestamo();
            prestamo.setId(rs.getInt("id"));
            prestamo.setUsuarioId(rs.getInt("usuario_id"));
            prestamo.setLibroId(rs.getInt("libro_id"));
            prestamo.setFechaPrestamo(LocalDate.parse(rs.getString("fecha_prestamo")));
            prestamo.setFechaDevolucionPrevista(LocalDate.parse(rs.getString("fecha_devolucion_prevista")));
            String fechaRealStr = rs.getString("fecha_devolucion_real");
            if (fechaRealStr != null) {
                prestamo.setFechaDevolucionReal(LocalDate.parse(fechaRealStr));
            } else {
                prestamo.setFechaDevolucionReal(null); // Si es null en la BD, queda null en Java sin romper
            }
            prestamo.setEstado(rs.getString("estado"));
            return prestamo;
        });
    }

    public List<Prestamo> findAllActive() {
        String sql = "SELECT * FROM prestamos WHERE estado = 'ACTIVO'";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Prestamo prestamo = new Prestamo();
            prestamo.setId(rs.getInt("id"));
            prestamo.setUsuarioId(rs.getInt("usuario_id"));
            prestamo.setLibroId(rs.getInt("libro_id"));
            prestamo.setFechaPrestamo(LocalDate.parse(rs.getString("fecha_prestamo")));
            prestamo.setFechaDevolucionPrevista(LocalDate.parse(rs.getString("fecha_devolucion_prevista")));
            prestamo.setFechaDevolucionReal(null); 
            prestamo.setEstado(rs.getString("estado"));
            return prestamo;
        });
    }
    
    public Prestamo findId(int id) {
        String sql = "SELECT * FROM prestamos WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Prestamo prestamo = new Prestamo();
                prestamo.setId(rs.getInt("id"));
                prestamo.setUsuarioId(rs.getInt("usuario_id"));
                prestamo.setLibroId(rs.getInt("libro_id"));
                prestamo.setFechaPrestamo(LocalDate.parse(rs.getString("fecha_prestamo")));
                prestamo.setFechaDevolucionPrevista(LocalDate.parse(rs.getString("fecha_devolucion_prevista")));
                String fechaRealStr = rs.getString("fecha_devolucion_real");
                if (fechaRealStr != null) {
                    prestamo.setFechaDevolucionReal(LocalDate.parse(fechaRealStr));
                } else {
                    prestamo.setFechaDevolucionReal(null); // Si es null en la BD, queda null en Java sin romper
                }
                prestamo.setEstado(rs.getString("estado"));
                return prestamo;
            }, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void save(Prestamo prestamo) {
        String sql = "INSERT INTO prestamos (usuario_id, libro_id, fecha_prestamo, fecha_devolucion_prevista, fecha_devolucion_real, estado) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            prestamo.getUsuarioId(),
            prestamo.getLibroId(), 
            prestamo.getFechaPrestamo(),
            prestamo.getFechaDevolucionPrevista(),
            prestamo.getFechaDevolucionReal(),
            prestamo.getEstado()
        );
    }

    public void update(Prestamo prestamo) {
        String sql = "UPDATE prestamos SET fecha_devolucion_real = ?, estado = ? WHERE id = ?";
        jdbcTemplate.update(sql,
            prestamo.getFechaDevolucionReal(),
            prestamo.getEstado(),
            prestamo.getId()
        );
    }
}