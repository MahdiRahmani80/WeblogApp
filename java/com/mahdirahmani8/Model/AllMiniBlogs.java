package com.mahdirahmani8.Model;

import java.util.List;

public class AllMiniBlogs {

    int count;
    String next,previous;
    List<MiniBlog> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<MiniBlog> getResults() {
        return results;
    }

    public void setResults(List<MiniBlog> results) {
        this.results = results;
    }
}
