package com.santiago.biblioteca;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.santiago.biblioteca.model.Libro;
import com.santiago.biblioteca.model.Usuario;
import com.santiago.biblioteca.model.Prestamo;
import com.santiago.biblioteca.service.LibroService;
import com.santiago.biblioteca.service.UsuarioService;
import com.santiago.biblioteca.service.PrestamoService; // Importante inyectarlo

@SpringBootApplication
public class BibliotecaApplication implements CommandLineRunner { 

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaApplication.class, args);
    }

    private Scanner scanner = new Scanner(System.in);

    @Autowired
    private LibroService libroService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private PrestamoService prestamoService; // Inyectamos el servicio de préstamos

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== BIBLIOTECA INICIADA ===");
        menuConsola();
    }
    
    private void menuConsola() {
        mostrarMenu();
    }

    private void mostrarMenu() {
        boolean salir = false;
        
        while (!salir) {
            System.out.println("\n=== SISTEMA DE BIBLIOTECA ===");
            System.out.println("1. Listar libros");
            System.out.println("2. Agregar libro");
            System.out.println("3. Listar usuarios");
            System.out.println("4. Agregar usuario");
            System.out.println("5. Registrar Préstamo");
            System.out.println("6. Registrar Devolución");
            System.out.println("7. Listar todos los préstamos");
            System.out.println("8. Salir");
            System.out.print("Elija una opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer
            
            switch (opcion) {
                case 1:
                    listarLibros();
                    break;
                case 2:
                    agregarLibro();
                    break;
                case 3:
                    listarUsuarios();
                    break;
                case 4:
                    agregarUsuario();
                    break;
                case 5:
                    solicitarPrestamo();
                    break;
                case 6:
                    devolverLibro();
                    break;
                case 7:
                    listarPrestamos();
                    break;
                case 8:
                    salir = true;
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
        scanner.close();
    }
    
    private void listarLibros() {
        List<Libro> libros = libroService.obtenerTodosLosLibros();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("\n--- LISTA DE LIBROS ---");
            for (Libro libro : libros) {
                System.out.println(libro.getId() + " | " + libro.getTitulo() + " | " + libro.getAutor() + " | Stock: " + libro.getStockDisponible() + "/" + libro.getStockTotal());
            }
        }
    }
    
    private void agregarLibro() {
        System.out.println("\n--- AGREGAR LIBRO ---");
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Año publicación: ");
        int anio = scanner.nextInt();
        System.out.print("Stock total: ");
        int stockTotal = scanner.nextInt();
        scanner.nextLine();
        
        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setIsbn(isbn);
        libro.setAnioPublicacion(anio);
        libro.setStockTotal(stockTotal);
        libro.setStockDisponible(stockTotal);
        
        libroService.guardar(libro);
        System.out.println("✓ Libro agregado exitosamente");
    }
    
    private void listarUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {
            System.out.println("\n--- LISTA DE USUARIOS ---");
            for (Usuario usuario : usuarios) {
                System.out.println(usuario.getId() + " | DNI: " + usuario.getDni() + " | " + usuario.getNombre() + " " + usuario.getApellido() + " | Estado: " + usuario.getEstado());
            }
        }
    }
    
    private void agregarUsuario() {
        System.out.println("\n--- AGREGAR USUARIO ---");
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();
        
        Usuario usuario = new Usuario();
        usuario.setDni(dni);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        usuario.setEstado("VALIDO"); // Lo creamos activo por defecto
        
        usuarioService.guardar(usuario);
        System.out.println("✓ Usuario agregado exitosamente");
    }

    private void solicitarPrestamo() {
        System.out.println("\n--- REGISTRAR PRÉSTAMO ---");
        System.out.print("Ingrese el ID del Usuario: ");
        int usuarioId = scanner.nextInt();
        System.out.print("Ingrese el ID del Libro: ");
        int libroId = scanner.nextInt();
        scanner.nextLine();

        try {
            // Llama a tu método con toda la lógica de negocio controlada
            prestamoService.registrarPrestamo(usuarioId, libroId);
            System.out.println("✓ Préstamo registrado exitosamente.");
        } catch (RuntimeException e) {
            // Si el libro no tiene stock o el usuario no existe, frena acá y muestra el por qué
            System.out.println("❌ Error al registrar préstamo: " + e.getMessage());
        }
    }

    private void devolverLibro() {
        System.out.println("\n--- REGISTRAR DEVOLUCIÓN ---");
        System.out.print("Ingrese el ID del Préstamo a devolver: ");
        int prestamoId = scanner.nextInt();
        scanner.nextLine();

        try {
            prestamoService.registrarDevolucion(prestamoId);
            System.out.println("✓ Libro devuelto correctamente. Stock actualizado.");
        } catch (RuntimeException e) {
            System.out.println("❌ Error al devolver: " + e.getMessage());
        }
    }

    private void listarPrestamos() {
        List<Prestamo> prestamos = prestamoService.obtenerTodosLosPrestamos();
        if (prestamos.isEmpty()) {
            System.out.println("No hay préstamos registrados.");
        } else {
            System.out.println("\n--- LISTA DE PRÉSTAMOS ---");
            for (Prestamo p : prestamos) {
                System.out.println("ID Prestamo: " + p.getId() + 
                                   " | Usuario ID: " + p.getUsuarioId() + 
                                   " | Libro ID: " + p.getLibroId() + 
                                   " | Estado: " + p.getEstado() + 
                                   " | F. Prestamo: " + p.getFechaPrestamo() + 
                                   " | F. Prevista: " + p.getFechaDevolucionPrevista());
            }
        }
    }
}