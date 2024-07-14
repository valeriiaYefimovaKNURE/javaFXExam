package org.example.kpp_exam;

public class User {
    protected final int id;
    protected final String name;
    protected boolean isBlocked;
    public User(int id, String name) {
        this.id = id;
        this.name = name;
        this.isBlocked = false;
    }
    public void BlockUser(){
        this.isBlocked=true;
    }
    public void UnblockUser(){
        this.isBlocked=false;
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
}
