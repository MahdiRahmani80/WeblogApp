package com.mahdirahmani8.Model;

public class MiniBlog {
    private int id,like;
    private String username,text,date,rep;
    private User user;

    private Republish republishd;

    public void setUser(User user) {
        this.user = user;
    }

    public void setRepublish( Republish republish) {
        this.republishd = republish;
    }

    public Republish getRepublishd() {
        return republishd;
    }


    public User getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user.setId(user);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRep() {
        return rep;
    }

    public void setRep(String rep) {
        this.rep = rep;
    }

    public class Republish{

        int id;
        String username;
        String text;
        int like;
        String date;
        int user;
        int republished;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getLike() {
            return like;
        }

        public void setLike(int like) {
            this.like = like;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getUser() {
            return user;
        }

        public void setUser(int user) {
            this.user = user;
        }

        public int getRepublished() {
            return republished;
        }

        public void setRepublished(int republished) {
            this.republished = republished;
        }
    }

}
