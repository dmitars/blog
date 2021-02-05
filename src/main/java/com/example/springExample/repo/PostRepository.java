package com.example.springExample.repo;

import com.example.springExample.models.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface PostRepository extends CrudRepository<Post,Long> {

}
