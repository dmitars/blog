package com.example.springExample.repo;

import com.example.springExample.models.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface PostRepository extends CrudRepository<Post,Long> {
    default Iterable<Post> findAllChecked() {
        var collection = this.findAll();
        return StreamSupport.stream(collection.spliterator(), false)
                .filter(Post::isChecked)
                .collect(Collectors.toList());
    }

    default Post findPostByOriginalId(Long id){
        var collection = this.findAll();
        for(Post post:collection){
            if(post.getOriginalId()!=null && post.getOriginalId().equals(id))
                return post;
        }
        return null;
    }

    default Iterable<Post> findAllUnchecked() {
        var collection = this.findAll();
        return StreamSupport.stream(collection.spliterator(), false)
                .filter(post -> !post.isChecked())
                .collect(Collectors.toList());
    }
}
