package com.santiago.biblioteca.service;

import java.util.List;

import com.santiago.biblioteca.model.Loan;

public interface ILoanService {

    // READ
    List<Loan> showLoans();
    Loan findId(int idLoan);
    
    // CREATE
    Loan addLoan(Loan loan);
    
    // DELETE
    boolean removeLoan(int idLoan);
    boolean registerReturn(int idLoan);
    
    //UPDATE
    Loan editLoan (Loan loan, int idLoan);
}
