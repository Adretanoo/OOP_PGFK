package com.jdbc.entity;

public class User {

    private Long id;
    private String login;
    private String email;
    private String password;
    private String address;
    private String phone;

    public User() {}

    public User(Long id, String login, String email, String password, String address, String phone) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
    }
    public User(String login, String email, String password, String address, String phone) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", login='" + login + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", address='" + address + '\'' +
            ", phone='" + phone + '\'' +
            '}';
    }
}
