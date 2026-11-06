package com.santiago.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor   
public class UserDto {
    private int id;
    private String dni;
    private String name;
    private String lastname;
    private String email;
    private String phone;
}
