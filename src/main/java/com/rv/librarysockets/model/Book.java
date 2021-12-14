package com.rv.librarysockets.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vitória Pizzutti
 */
public class Book {

    private String title;
    private String isbn;
    private String genre;
    private Integer edition;
    private String publishingCompany;
    private Integer year;
    private List<String> authors = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getEdition() {
        return edition;
    }

    public void setEdition(Integer edition) {
        this.edition = edition;
    }

    public String getPublishingCompany() {
        return publishingCompany;
    }

    public void setPublishingCompany(String publishingCompany) {
        this.publishingCompany = publishingCompany;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "Título: " + title + "\n"
                + "ISBN: " + isbn + "\n"
                + "Gênero: " + genre + "\n"
                + "Editora: " + publishingCompany + "\n"
                + "Edição: " + edition + "ª\n"
                + "Ano: " + year + "\n"
                + "Autores: " + String.join(", ", authors) + "\n\n";
    }

}
