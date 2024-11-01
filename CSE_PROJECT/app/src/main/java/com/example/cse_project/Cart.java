package com.example.cse_project;
public class Cart {
    private String bookId;
    private String bookImage;
    private String bookTitle;
    private int totalQuantity;
    private double pricePerItem;
    private double totalPrice;


    public Cart(String bookId, String bookImage, String bookTitle, int totalQuantity, double pricePerItem) {
        this.bookId = bookId;
        this.bookImage = bookImage;
        this.bookTitle = bookTitle;
        this.totalQuantity = totalQuantity;
        this.pricePerItem = pricePerItem;
        this.totalPrice = pricePerItem * totalQuantity;
    }

    // Getters and Setters
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
        this.totalPrice = pricePerItem * totalQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getPricePerItem() {
        return pricePerItem; // Trả về giá mỗi sản phẩm
    }

    public void setPricePerItem(double pricePerItem) {
        this.pricePerItem = pricePerItem;
        this.totalPrice = pricePerItem * totalQuantity;
    }
}

