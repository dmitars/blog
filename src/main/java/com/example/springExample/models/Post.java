package com.example.springExample.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post implements Datable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String anons;
    private String fullText;
    private String date;

    @OneToMany(targetEntity=Comment.class, cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    private Long originalId;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    private String author;
    private int views;
    private boolean isChecked = false;

    public Post(){}

    public Post(String title, String anons, String full_text, String author) {
        setAuthor(author);
        setOriginalId(getId());
        updateDate();
        setTitle(title);
        setAnons(anons);
        setFullText(full_text);
    }

    public Post(String title, String anons, String full_text, String author, Long originalId) {
        this(title,anons,full_text,author);
        setOriginalId(originalId);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = "посл. изменение: "+date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnons() {
        return anons;
    }

    public void setAnons(String anons) {
        this.anons = anons;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }

    public void removeCommentById(long commentId){
        for(var comment:comments){
            if(comment.getId() == commentId){
                comments.remove(comment);
                break;
            }
        }
    }

    public List<Comment>getComments(){
        return comments;
       // return null;
    }

}
