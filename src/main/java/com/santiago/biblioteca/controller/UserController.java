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

import com.santiago.biblioteca.model.User;
import com.santiago.biblioteca.service.IUserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private IUserService userService;

    public UserController (IUserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> showUsers () {
        
        return userService.showUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findId (@PathVariable int id) {
        User user = userService.findId(id);
        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encuentra usuario con ese id");
        }
        return ResponseEntity.ok(user);
    }
    @PostMapping()
    public ResponseEntity<?> addUser(@RequestBody User user) {
        User userCreated = userService.addUser(user);
        if(userCreated == null) {
            return ResponseEntity.badRequest().body("Los datos del user no son validos");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUser (@PathVariable int id, @RequestBody User user) {
        User editedUser = userService.editUser(id, user);
        if(editedUser == null) {
            return ResponseEntity.badRequest().body("Los datos nuevos del user no son validos");
        }
        return ResponseEntity.ok(editedUser);
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
