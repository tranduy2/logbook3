package com.example.contactdatabaseapp;

public class Contact {
    private int id;
    private String name;
    private String phone;
    private String email;
    private int avatarId;

    public Contact(int id, String name, String phone, String email, int avatarId) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.avatarId = avatarId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public int getAvatarId() { return avatarId; }
}