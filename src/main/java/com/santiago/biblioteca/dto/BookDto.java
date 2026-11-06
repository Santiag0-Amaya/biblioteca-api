package com.santiago.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private int id;
    private String title;
    private String author;
    private String isbn;
    private int publicationDate;
    private int totalStock;
    private int availableStock;
}
