package com.mahdirahmani8.Model;

import java.util.List;

public class HomeAllBlog {

    int count;
    String next,previous;
    List<Blog> results;

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

    public List<Blog> getResults() {
        return results;
    }

    public void setResults(List<Blog> results) {
        this.results = results;
    }
}
