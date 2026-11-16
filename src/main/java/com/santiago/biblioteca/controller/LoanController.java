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

import com.santiago.biblioteca.dto.LoanCreateRequestDto;
import com.santiago.biblioteca.dto.LoanRequestDto;
import com.santiago.biblioteca.dto.LoanUpdateRequestDto;
import com.santiago.biblioteca.model.Book;
import com.santiago.biblioteca.model.Loan;
import com.santiago.biblioteca.model.User;
import com.santiago.biblioteca.service.ILoanService;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    
    private ILoanService loanService;

    public LoanController (ILoanService loanService) {
        this.loanService = loanService;
    }

    private LoanRequestDto toDto (Loan loan){
        return new LoanRequestDto(
            loan.getId(),
            loan.getBook().getTitle(),
            loan.getUser().getDni(),
            loan.getLoanDate(),
            loan.getExpectedReturnDate(),
            loan.getRealReturnDate(),
            loan.getStatus()
        );
    }

    private Loan toEntity(LoanCreateRequestDto loanRequestDto) {
        User user = new User();
        user.setId(loanRequestDto.getUserId());

        Book book = new Book();
        book.setId(loanRequestDto.getBookId());

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setExpectedReturnDate(loanRequestDto.getExpectedReturnDate());
        return loan;
    }

    private Loan toEntity(LoanUpdateRequestDto loanRequestDto) {
        Loan loan = new Loan();
        loan.setExpectedReturnDate(loanRequestDto.getExpectedReturnDate());
        return loan;
    }
    
    @GetMapping()
    public List<LoanRequestDto> showLoans () {
        return loanService.showLoans().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findId (@PathVariable int id) {
        Loan loan = loanService.findId(id);
        if(loan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("No se encuentra un prestamo con esa id");
        }
        return ResponseEntity.ok(toDto(loan));
    }

    @PostMapping()
    public ResponseEntity<?> addLoan(@RequestBody LoanCreateRequestDto loanRequestDto) {
        if (loanRequestDto == null
                || loanRequestDto.getUserId() == null
                || loanRequestDto.getUserId() <= 0
                || loanRequestDto.getBookId() == null
                || loanRequestDto.getBookId() <= 0) {
            return ResponseEntity.badRequest().body("Los datos del prestamo no son validos");
        }
        
        Loan addedLoan = loanService.addLoan(toEntity(loanRequestDto));
        if(addedLoan == null) {
            return ResponseEntity.badRequest().body("Los datos del prestamo no son validos");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(addedLoan));
    }
    
    @PutMapping ("/{id}")
    public ResponseEntity<?> editLoan(@PathVariable int id, @RequestBody LoanUpdateRequestDto loanRequestDto) {
        if (loanRequestDto == null || loanRequestDto.getExpectedReturnDate() == null) {
            return ResponseEntity.badRequest().body("Los datos del prestamo no son validos");
        }
        Loan editedLoan = loanService.editLoan(toEntity(loanRequestDto), id);
        if(editedLoan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Los datos nuevos no son validos");
        }
        return ResponseEntity.ok(toDto(editedLoan));
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
