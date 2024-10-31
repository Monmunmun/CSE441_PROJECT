package com.example.cse_project;

public class Cart {
    private String bookId;
    private String bookImage;
    private String bookTitle;
    private int totalQuantity;
    private double totalPrice;


  
    public Cart() {
    }


    public Cart(String bookId, String bookImage, String bookTitle, int totalQuantity, double totalPrice) {
        this.bookId = bookId;
        this.bookImage = bookImage;
        this.bookTitle = bookTitle;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;

    }

    // Getters và Setters
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQuantity() {
        return totalQuantity;
    }

    public double getPrice() {
        return totalPrice;
    }

    public String getImageUrl() {
        return bookImage;
    }

    public String getTitle() {
        return bookTitle;
    }
    public double getPricePerItem() {
        return totalQuantity > 0 ? totalPrice / totalQuantity : 0;
    }
}
