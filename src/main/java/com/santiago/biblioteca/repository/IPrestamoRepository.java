package com.santiago.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.santiago.biblioteca.model.Prestamo;

@Repository
public interface IPrestamoRepository extends JpaRepository<Prestamo, Integer> {

}
