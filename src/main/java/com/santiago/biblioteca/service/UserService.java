package com.santiago.biblioteca.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.santiago.biblioteca.model.User;
import com.santiago.biblioteca.repository.IUserRepository;

@Service
public class UserService implements IUserService {

    private IUserRepository userRepository;

    @Override
    public boolean existUser (String dni) {
        return userRepository.existsByDni(dni);
    }

    public UserService (IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> showUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findId(int idUser) {
        return userRepository.findById(idUser).orElse(null);
    }

    @Override
    public User addUser(User user) {
        if(user == null || existUser(user.getDni()) ) return null;
        user.setDeleted(false);
        user.setStatus("VALIDO");

        return userRepository.save(user);

    }

    @Override
    public boolean removeUser(int idUser) {
        User userExistente = findId(idUser);
        if(userExistente == null) return false;
        userRepository.delete(userExistente);
        return true;
    }

    @Override
    public boolean logicallyDeleteUser(int idUser) {
        User userExistente = findId(idUser);
        if(userExistente == null) return false;
        userExistente.setDeleted(true);
        userRepository.save(userExistente);
        return true;
    }

    @Override
    public User editUser(int idUser, User user) {
        User userExistente = findId(idUser);
        if (userExistente == null || user == null) return null;
        if (!userExistente.getDni().equals(user.getDni()) && existUser(user.getDni())) return null;

        userExistente.setDni(user.getDni()); 
        userExistente.setName(user.getName()); 
        userExistente.setLastname(user.getLastname());
        userExistente.setEmail(user.getEmail());
        userExistente.setPhone(user.getPhone());

        return userRepository.save(userExistente);
    }

}
