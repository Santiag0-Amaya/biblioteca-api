package com.santiago.biblioteca.controller;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
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

import com.santiago.biblioteca.dto.UserRequestDto;
import com.santiago.biblioteca.model.User;
import com.santiago.biblioteca.service.IUserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private IUserService userService;

    public UserController (IUserService userService) {
        this.userService = userService;
    }

    private UserRequestDto toDto(User user) {
        return new UserRequestDto(
                user.getId(),
                user.getDni(),
                user.getName(),
                user.getLastname(),
                user.getEmail(),
                user.getPhone()
        );
    }

    private User toEntity(UserRequestDto dto) {
        User user = new User();
        user.setDni(dto.getDni());
        user.setName(dto.getName());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        return user;
    }

    @GetMapping()
    public List<UserRequestDto> showUsers () {
        return userService.showUsers().stream().map(this::toDto).collect(java.util.stream.Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findId (@PathVariable int id) {
        User user = userService.findId(id);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encuentra usuario con ese id");
        }
        return ResponseEntity.ok(toDto(user));
    }
    @PostMapping()
    public ResponseEntity<?> addUser(@RequestBody UserRequestDto userRequestDto) {
        if (userRequestDto == null || userRequestDto.getDni() == null || userRequestDto.getDni().isBlank()) {
            return ResponseEntity.badRequest().body("El Dni del usuario es obligatorio");
        }

        if (userService.existUser(userRequestDto.getDni())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un usuario con ese Dni");
        }
        User userCreated;
        try {
            userCreated =  userService.addUser(toEntity(userRequestDto));
        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un usuario con ese Dni");
        }
        if (userCreated == null) {
            return ResponseEntity.badRequest().body("Los datos del usuario no son validos");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUser (@PathVariable int id, @RequestBody UserRequestDto userRequestDto) {
        if (userRequestDto == null || userRequestDto.getDni() == null || userRequestDto.getDni().isBlank()) {
            return ResponseEntity.badRequest().body("El Dni del usuario es obligatorio");
        }
        if (userService.existUser(userRequestDto.getDni())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un usuario con ese Dni");
        }
        User userEdited;
        try {
            userEdited = userService.editUser(id, toEntity(userRequestDto)); 
        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un usuario con ese dni");
        }
        if (userEdited == null) {
            return ResponseEntity.badRequest().body("No se pudo editar");
        }

        return ResponseEntity.ok(toDto(userEdited));
    }

    @PutMapping("/{id}/logically-delete")
    public ResponseEntity <?> logicallyDeletedUser (@PathVariable int id) {
        Boolean userRemoved = userService.logicallyDeleteUser(id);
        if(!userRemoved) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("No se encuentra el user a eliminar");
        }
        return ResponseEntity.ok(userRemoved);
    }

    @PutMapping ("/{id}/status")
    public ResponseEntity <?> changeUserStatus (@PathVariable int id, @RequestBody User user) {
        Boolean statusChanged = userService.changeUserStatus(id, user.getStatus());
        if(!statusChanged) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("No se encuentra el user para cambiar su estado");
        }
        return ResponseEntity.ok(statusChanged);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <?> removedUser (@PathVariable int id) {
        Boolean userRemoved = userService.removeUser(id);
        if(!userRemoved) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("No se encuentra el user a eliminar");
        }
        return ResponseEntity.ok(userRemoved);
    }

}
