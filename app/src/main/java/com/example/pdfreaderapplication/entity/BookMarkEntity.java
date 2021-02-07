package com.example.pdfreaderapplication.entity;

public class BookMarkEntity {
    int bookMarkId;
    String bookMarkFileName;

    public BookMarkEntity(int bookMarkId, String bookMarkFileName) {
        this.bookMarkId = bookMarkId;
        this.bookMarkFileName = bookMarkFileName;
    }

    public int getBookMarkId() {
        return bookMarkId;
    }

    public void setBookMarkId(int bookMarkId) {
        this.bookMarkId = bookMarkId;
    }

    public String getBookMarkFileName() {
        return bookMarkFileName;
    }

    public void setBookMarkFileName(String bookMarkFileName) {
        this.bookMarkFileName = bookMarkFileName;
    }
}
