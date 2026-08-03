package com.santiago.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.santiago.biblioteca.model.Usuario;

@Repository
public interface IUserRepository extends JpaRepository<Usuario, Integer> {

}
