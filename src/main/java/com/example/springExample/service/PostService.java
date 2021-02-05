package com.example.springExample.service;

import com.example.springExample.models.Post;
import com.example.springExample.models.User;
import com.example.springExample.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PostService {

    final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Iterable<Post> findAllChecked() {
        var collection = postRepository.findAll();
        return StreamSupport.stream(collection.spliterator(), false)
                .filter(Post::isChecked)
                .collect(Collectors.toList());
    }

    public Iterable<Post> findAllUnchecked() {
        var collection = postRepository.findAll();
        return StreamSupport.stream(collection.spliterator(), false)
                .filter(post -> !post.isChecked())
                .collect(Collectors.toList());
    }

    public Iterable<Post> findLastPosts(int number){
        var posts = findAllChecked();
        return StreamSupport.stream(posts.spliterator(),false)
                .limit(number)
                .collect(Collectors.toList());
    }

    public Post findPostByOriginalId(Long id){
        var collection = postRepository.findAll();
        return StreamSupport.stream(collection.spliterator(), false)
                .filter(post -> post.getOriginalId()!=null && post.getOriginalId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addPost(Post post, User author){
        post.setAuthor(author.getUsername());
        post.updateDate();
        postRepository.save(post);
    }

    public Post addPostToEdited(Post post){
        if(!postRepository.existsById(post.getId()))
            return null;
        Post postFromDB = postRepository.findById(post.getId()).get();
        postFromDB.incrementViews();
        postRepository.save(postFromDB);
        return postFromDB;
    }

    public void updatePost(Post post){
        if(post.getOriginalId()!=null)
            postRepository.deleteById(post.getOriginalId());
        post.updateDate();
        post.setChecked(true);
        postRepository.save(post);
    }

}
