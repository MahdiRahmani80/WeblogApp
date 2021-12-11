package com.mahdirahmani8.Model;

import java.util.List;

public class MiniBlogFeed {
    int id;
    User user;
    List<MiniBlog> miniblog;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<MiniBlog> getMiniblog() {
        return miniblog;
    }

    public void setMiniblog(List<MiniBlog> miniblog) {
        this.miniblog = miniblog;
    }
}
