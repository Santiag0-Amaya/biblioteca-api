package com.santiago.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.santiago.biblioteca.model.Loan;

@Repository
public interface ILoanRepository extends JpaRepository<Loan, Integer> {

}
