package com.mahdirahmani8.Model;

import java.util.List;

public class followers {
    int user,id;
    List<Integer> another_user;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getAnother_user() {
        return another_user;
    }

    public void setAnother_user(List<Integer> another_user) {
        this.another_user = another_user;
    }
}
