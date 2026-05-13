package com.santiago.biblioteca;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.santiago.biblioteca.model.Libro;
import com.santiago.biblioteca.model.Usuario;
import com.santiago.biblioteca.service.LibroService;
import com.santiago.biblioteca.service.UsuarioService;

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
	

	@Override
	public void run(String... args) throws Exception {
		// El menú va AQUÍ
		System.out.println("=== BIBLIOTECA INICIADA ===");
		// podés llamar a un método con el menú
		menuConsola();
	}
	
	private void menuConsola() {
		// acá va todo el menú
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
            System.out.println("5. Salir");
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
                System.out.println(usuario.getId() + " | " + usuario.getNombre() + " | " + usuario.getEmail() + " | " + usuario.getTelefono());
            }
        }
    }
    
    private void agregarUsuario() {
        System.out.println("\n--- AGREGAR USUARIO ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
		System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();
        
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
		usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        
        usuarioService.guardar(usuario);
        System.out.println("✓ Usuario agregado exitosamente");
    }
}