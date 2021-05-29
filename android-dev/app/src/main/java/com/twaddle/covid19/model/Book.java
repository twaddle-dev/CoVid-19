package com.twaddle.covid19.model;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Book {
    @SerializedName("bid")
    private int bid;
    @SerializedName("bookName")
    private String bookName;
    @SerializedName("authorName")
    private String authorName;

    public Book(int bid, String bookName, String authorName) {
        this.bid = bid;
        this.bookName = bookName;
        this.authorName = authorName;
    }

    @NonNull
    @Override
    public String toString() {
        return "Book{" +
                "bid=" + bid +
                ", bookName='" + bookName + '\'' +
                ", authorName='" + authorName + '\'' +
                '}';
    }

    public Book(){

    }
    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}

