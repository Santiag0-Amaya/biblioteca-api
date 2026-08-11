package com.santiago.biblioteca.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.santiago.biblioteca.model.Loan;
import com.santiago.biblioteca.service.ILoanService;

@RestController
@RequestMapping("/api/Loans")
public class LoanController {
    
    private ILoanService loanService;

    public LoanController (ILoanService loanService) {
        this.loanService = loanService;
    }
    
    @GetMapping()
    public List<Loan> showUsers () {
        
        return loanService.showLoans();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findId (@PathVariable int id) {
        Loan loan = loanService.findId(id);
        if(loan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("No se encuentra un prestamo con esa id");
        }
        return ResponseEntity.ok(loan);
    }

    @PostMapping()
    public ResponseEntity <?> addLoan(@RequestBody Loan loan) {
        Loan addedLoan = loanService.addLoan(loan);
        if(addedLoan == null) {
            return ResponseEntity.badRequest().body("Los datos del prestamo no son validos");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(addedLoan);
    }
    
    @PutMapping ("/{id}")
    public ResponseEntity<?> editLoan (@PathVariable int id, @RequestBody Loan loan) {
        Loan editedLoan = loanService.editLoan(loan, id);
        if(editedLoan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Los datos nuevos no son validos");
        }
        return ResponseEntity.ok(editedLoan);
    }
    @PutMapping ("/{id}/register-return")
    public ResponseEntity<?> registerReturn (@PathVariable int id) {
        boolean returnedLoan = loanService.registerReturn(id);
        if(!returnedLoan) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo registrar la devolucion");
        }
        return ResponseEntity.ok("Devolucion registrada correctamente");
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<?> removeLoan (@PathVariable int id) {
        boolean removedLoan = loanService.removeLoan(id);
        if(!removedLoan) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo eliminar");
        }
        return ResponseEntity.ok("Producto eliminado correctamente");
    }


}
