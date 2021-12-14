package com.rv.librarysockets.model;

/**
 *
 * @author Vitória Pizzutti
 */
public class ClientRequest {

    private int operation;
    private String value;
    private Book book;

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
    
}
