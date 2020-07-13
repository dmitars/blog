package com.example.springExample.models;

import javax.persistence.*;

@Entity
public class Comment implements Datable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String text;
    String author;
    String date;

    public Comment(){}

    public Comment(String text,String username){
        setText(text);
        updateDate();
        setAuthor(username);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
