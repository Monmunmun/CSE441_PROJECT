package com.example.cse_project;

public class Users {
    private String key;  // Dùng key thay cho ID
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String password;
    private double money;  // Thuộc tính money
    private String role;   // Thuộc tính role
    private String avatar; // Thuộc tính avatar

    // Constructor với key
    public Users(String key, String name, String email, String phoneNumber, String address, String password, double money, String role, String avatar) {
        this.key = key;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        this.money = money;
        this.role = role;
        this.avatar = avatar; // Khởi tạo avatar
    }

    // Constructor mặc định cần thiết cho Firebase
    public Users() {
    }

    // Getter và Setter cho key
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    // Getter và Setter cho các thuộc tính khác
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter và Setter cho money
    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    // Getter và Setter cho role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Getter và Setter cho avatar
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Users{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", money=" + money +
                ", role='" + role + '\'' +
                ", avatar='" + avatar + '\'' + // Hiển thị avatar trong toString
                '}';
    }
}
