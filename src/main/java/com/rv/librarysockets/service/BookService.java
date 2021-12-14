package com.rv.librarysockets.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rv.librarysockets.model.Book;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Vitória Pizzutti
 */
public class BookService {

    private List<Book> books = new ArrayList();

    public void readJson() {
        try {
            File file = new File("./books.json");
            ObjectMapper objectMapper = new ObjectMapper();
            books = objectMapper.readValue(file, new TypeReference<List<Book>>() {});
        } catch (IOException ex) {
            System.out.println("Erro ao ler arquivo JSON: " + ex.getMessage());
        }
    }

    public void writeJson() {
        try {
            File file = new File("./books.json");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(file, books);
        } catch (IOException ex) {
            System.out.println("Erro ao escrever arquivo JSON: " + ex.getMessage());
        }
    }

    public void addNewBook(Book book) {
        books.add(book);
    }

    public List<Book> findBooksByTitle(String bookTitle) {
        return books.stream().filter(
                book -> book.getTitle().toLowerCase().contains(bookTitle.toLowerCase())
        ).collect(Collectors.toList());
    }

    public Book findBookByIsbn(String isbn) {
        return books.stream().filter(book -> isbn.equals(book.getIsbn()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> list.get(0)));
    }

    public boolean deleteBookByIsbn(String isbn) {
        return books.removeIf(book-> isbn.equals(book.getIsbn()));
    }

}
