package com.example.springExample.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post implements DatedData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Заголовок не должен быть пуст")
    private String title;

    @NotBlank(message = "Описание не должен быть пустым")
    private String anons;

    @NotBlank(message = "Текст не должен быть пуст")
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

    public Post(String title, String anons, String full_text) {
        setOriginalId(getId());
        setTitle(title);
        setAnons(anons);
        setFullText(full_text);
    }

    public Post(String title, String anons, String full_text,  Long originalId) {
        this(title,anons,full_text);
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

    public void incrementViews(){
        views++;
    }

    public void removeCommentById(long commentId){
        comments.removeIf(comment -> comment.getId() == commentId);
    }

    public List<Comment>getComments(){
        return comments;
    }

    public void updateAuthor(User newAuthor){
        if (!(author.equals(newAuthor.getUsername()) || author.contains("," + newAuthor.getUsername() + ",")))
            author = author + "," + newAuthor.getUsername();
        if(author.length()>25)
            author = author.substring(0,25)+",..";
    }

}
