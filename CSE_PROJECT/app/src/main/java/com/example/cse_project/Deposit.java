package com.example.cse_project;

public class Deposit {
    private String CardID;
    private String userDeposit;
    private String typecard;
    private double amount;
    private String cardseri;
    private String cardcode;
    private String cardDate;

    public Deposit() {
    }

    // Constructor
    public Deposit(String cardID, String userDeposit, String typecard, double amount, String cardseri, String cardcode, String cardDate) {
        this.CardID = cardID;
        this.userDeposit = userDeposit;
        this.typecard = typecard;
        this.amount = amount;
        this.cardseri = cardseri;
        this.cardcode = cardcode;
        this.cardDate = cardDate;
    }

    // Getters and Setters
    public String getCardID() {
        return CardID;
    }

    public void setCardID(String cardID) {
        this.CardID = cardID;
    }

    public String getUserDeposit() {
        return userDeposit;
    }

    public void setUserDeposit(String userDeposit) {
        this.userDeposit = userDeposit;
    }

    public String getTypecard() {
        return typecard;
    }

    public void setTypecard(String typecard) {
        this.typecard = typecard;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCardseri() {
        return cardseri;
    }

    public void setCardseri(String cardseri) {
        this.cardseri = cardseri;
    }

    public String getCardcode() {
        return cardcode;
    }

    public void setCardcode(String cardcode) {
        this.cardcode = cardcode;
    }

    public String getCardDate() {
        return cardDate;
    }

    public void setCardDate(String cardDate) {
        this.cardDate = cardDate;
    }
}
