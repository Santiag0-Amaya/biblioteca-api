package com.santiago.biblioteca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santiago.biblioteca.model.Book;
import com.santiago.biblioteca.model.Loan;
import com.santiago.biblioteca.model.User;
import com.santiago.biblioteca.repository.IBookRepository;
import com.santiago.biblioteca.repository.ILoanRepository;
import com.santiago.biblioteca.repository.IUserRepository;
import com.santiago.biblioteca.service.ILoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:biblioteca-test;MODE=MySQL;DB_CLOSE_DELAY=-1;NON_KEYWORDS=USER",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=never"
})
class BibliotecaApplicationTests {

    @Autowired
    private IBookRepository bookRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ILoanRepository loanRepository;

    @Autowired
    private ILoanService loanService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDatabase() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertNotNull(loanService);
    }

    @Test
    void createsLoanAndDecreasesAvailableStock() {
        User user = userRepository.saveAndFlush(newUser("12345678"));
        Book book = bookRepository.saveAndFlush(newBook("9780000000001", 1));

        Loan loan = loanService.addLoan(newLoan(user, book));

        assertNotNull(loan);
        assertEquals(user.getId(), loan.getUser().getId());
        assertEquals(book.getId(), loan.getBook().getId());
        assertEquals(0, bookRepository.findById(book.getId()).orElseThrow().getAvailableStock());
        assertEquals(1, loanRepository.count());
    }

    @Test
    void rejectsLoanWhenNoCopiesAreAvailable() {
        User user = userRepository.saveAndFlush(newUser("12345678"));
        Book book = bookRepository.saveAndFlush(newBook("9780000000001", 0));

        Loan loan = loanService.addLoan(newLoan(user, book));

        assertNull(loan);
        assertEquals(0, bookRepository.findById(book.getId()).orElseThrow().getAvailableStock());
        assertEquals(0, loanRepository.count());
    }

    @Test
    void concurrentLoansCannotBorrowTheLastCopyTwice() throws Exception {
        User firstUser = userRepository.saveAndFlush(newUser("12345678"));
        User secondUser = userRepository.saveAndFlush(newUser("87654321"));
        Book book = bookRepository.saveAndFlush(newBook("9780000000001", 1));

        List<Loan> results = runConcurrently(
                () -> loanService.addLoan(newLoan(firstUser, book)),
                () -> loanService.addLoan(newLoan(secondUser, book)));

        assertEquals(1, results.stream().filter(loan -> loan != null).count());
        assertEquals(1, loanRepository.count());
        assertEquals(0, bookRepository.findById(book.getId()).orElseThrow().getAvailableStock());
    }

    @Test
    void returningLoanRestoresStockAndCannotBeDoneTwice() {
        User user = userRepository.saveAndFlush(newUser("12345678"));
        Book book = bookRepository.saveAndFlush(newBook("9780000000001", 1));
        Loan loan = loanService.addLoan(newLoan(user, book));

        assertTrue(loanService.registerReturn(loan.getId()));

        Loan returnedLoan = loanRepository.findById(loan.getId()).orElseThrow();
        assertEquals("DEVUELTO", returnedLoan.getStatus());
        assertEquals(LocalDate.now(), returnedLoan.getRealReturnDate());
        assertEquals(1, bookRepository.findById(book.getId()).orElseThrow().getAvailableStock());

        assertFalse(loanService.registerReturn(loan.getId()));
        assertEquals(1, bookRepository.findById(book.getId()).orElseThrow().getAvailableStock());
    }

    @Test
    void concurrentReturnsIncreaseStockOnlyOnce() throws Exception {
        User user = userRepository.saveAndFlush(newUser("12345678"));
        Book book = bookRepository.saveAndFlush(newBook("9780000000001", 1));
        Loan loan = loanService.addLoan(newLoan(user, book));

        List<Boolean> results = runConcurrently(
                () -> loanService.registerReturn(loan.getId()),
                () -> loanService.registerReturn(loan.getId()));

        assertEquals(1, results.stream().filter(Boolean::booleanValue).count());
        assertEquals(1, bookRepository.findById(book.getId()).orElseThrow().getAvailableStock());
    }

    @Test
    void databaseEnforcesUniqueIsbnAndDni() {
        bookRepository.saveAndFlush(newBook("9780000000001", 1));
        userRepository.saveAndFlush(newUser("12345678"));

        assertThrows(DataIntegrityViolationException.class,
                () -> bookRepository.saveAndFlush(newBook("9780000000001", 1)));
        assertThrows(DataIntegrityViolationException.class,
                () -> userRepository.saveAndFlush(newUser("12345678")));
    }

    @Test
    void duplicateIsbnReturnsConflictWithHelpfulMessage() throws Exception {
        bookRepository.saveAndFlush(newBook("9780000000001", 1));
        Book duplicate = newBook("9780000000001", 1);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Ya existe un libro con ese ISBN"));
    }

    private Book newBook(String isbn, int availableStock) {
        Book book = new Book();
        book.setTitle("Libro de prueba");
        book.setAuthor("Autor de prueba");
        book.setIsbn(isbn);
        book.setPublicationDate(2020);
        book.setTotalStock(availableStock);
        book.setAvailableStock(availableStock);
        return book;
    }

    private User newUser(String dni) {
        User user = new User();
        user.setDni(dni);
        user.setName("Nombre");
        user.setLastname("Apellido");
        user.setEmail("test@example.com");
        user.setPhone("123456789");
        user.setStatus("VALIDO");
        return user;
    }

    private Loan newLoan(User user, Book book) {
        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(LocalDate.now());
        loan.setExpectedReturnDate(LocalDate.now().plusDays(14));
        loan.setStatus("ACTIVO");
        return loan;
    }

    private <T> List<T> runConcurrently(Callable<T> firstAction, Callable<T> secondAction)
            throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        Callable<T> first = awaitStart(firstAction, ready, start);
        Callable<T> second = awaitStart(secondAction, ready, start);

        try {
            Future<T> firstResult = executor.submit(first);
            Future<T> secondResult = executor.submit(second);
            assertTrue(ready.await(5, TimeUnit.SECONDS), "Las tareas deben quedar listas");
            start.countDown();

            List<T> results = new ArrayList<>(2);
            results.add(firstResult.get(10, TimeUnit.SECONDS));
            results.add(secondResult.get(10, TimeUnit.SECONDS));
            return results;
        } finally {
            start.countDown();
            executor.shutdownNow();
        }
    }

    private <T> Callable<T> awaitStart(
            Callable<T> action, CountDownLatch ready, CountDownLatch start) {
        return () -> {
            ready.countDown();
            if (!start.await(5, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Tiempo agotado esperando el inicio concurrente");
            }
            return action.call();
        };
    }
}
