package com.santiago.biblioteca.dto;

import java.time.LocalDate;

public class LoanDto {
    private int id;
    private String bookTitle;
    private String userDni;
    private LocalDate loanDate;
    private LocalDate expectedReturnDate;
    private LocalDate realReturnDate;
    private String status;  // activo-vencido-devuelto
}
