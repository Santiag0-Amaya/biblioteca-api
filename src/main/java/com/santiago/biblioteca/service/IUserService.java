package com.santiago.biblioteca.service;

import java.util.List;

import com.santiago.biblioteca.model.User;

public interface IUserService {

    // READ
    List<User> showUsers();
    User findId(int idUser);
    boolean existUser (String dni);
    
    // CREATE
    User addUser(User user);
    
    // DELETE
    boolean removeUser(int idUser);
    boolean logicallyDeleteUser(int idUser); // logico
    
    //UPDATE
    User editUser (int idUser, User user);
    boolean changeUserStatus(int idUser, String status);

}
