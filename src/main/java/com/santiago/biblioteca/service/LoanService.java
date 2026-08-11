package com.santiago.biblioteca.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.santiago.biblioteca.model.Book;
import com.santiago.biblioteca.model.Loan;
import com.santiago.biblioteca.model.User;
import com.santiago.biblioteca.repository.IBookRepository;
import com.santiago.biblioteca.repository.ILoanRepository;
import com.santiago.biblioteca.repository.IUserRepository;
@Service
public class LoanService implements ILoanService{

    private ILoanRepository loanRepository;
    private IBookRepository bookRepository;
    private IUserRepository userRepository;
    public LoanService (ILoanRepository loanRepository, IBookRepository bookRepository, IUserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<Loan> showLoans() {
        return loanRepository.findAll();
    }

    @Override
    public Loan findId(int idLoan) {
        return loanRepository.findById(idLoan).orElse(null);
    }

    @Override
    public Loan addLoan(Loan loan) {
        if(loan == null) return null;

        int idUser = loan.getUser().getId();
        User user = userRepository.findById(idUser).orElse(null);
        if(user == null) return null;

        int idBook = loan.getBook().getId();
        Book book = bookRepository.findById(idBook).orElse(null);
        if(book == null) return null;
        if(book.getAvailableStock()<=0) return null;

        // pongo los Books y Users reales traidos de la bd. tenia solo sus id
        loan.setBook(book);
        loan.setUser(user);

        book.setAvailableStock(book.getAvailableStock()-1);

        // actualizo el stock
        bookRepository.save(book);

        return loanRepository.save(loan);

    }

    @Override
    public boolean removeLoan(int idLoan) {
        Loan loan = loanRepository.findById(idLoan).orElse(null);
        if(loan == null) return false;
        loanRepository.delete(loan);
        return true;
    }

    @Override
    public Loan editLoan(Loan loan, int idLoan) {
        
        Loan loanExisting = findId(idLoan);

        if (loanExisting == null || loan == null) {
            return null;
        }
        loanExisting.setExpectedReturnDate(loan.getExpectedReturnDate());

        return loanRepository.save(loanExisting);
    }

    @Override
    public boolean registerReturn(int idLoan) {

        Loan loan = loanRepository.findById(idLoan)
                .orElse(null);

        if (loan == null) {
            return false;
        }

        if (loan.getStatus().equals("RETURNED")) {
            return false;
        }

        Book book = loan.getBook();

        book.setAvailableStock( book.getAvailableStock() + 1);

        bookRepository.save(book);

        loan.setStatus("DEVUELTO");
        loan.setRealReturnDate(LocalDate.now());

        loanRepository.save(loan);

        return true;
    }



}
