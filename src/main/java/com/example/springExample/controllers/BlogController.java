package com.example.springExample.controllers;

import com.example.springExample.models.Comment;
import com.example.springExample.models.Post;
import com.example.springExample.models.User;
import com.example.springExample.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {
    String message="";

    @Autowired(required = true)
    private PostRepository postRepository;

    @GetMapping("/blog")
    public String blog(@AuthenticationPrincipal User user, Model model) {
        Iterable<Post> posts = postRepository.findAllChecked();
        model.addAttribute("posts",posts);
        if(!message.equals("")){
            model.addAttribute("msg",message);
            message = "";
        }
        model.addAttribute("title","Блог");
        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title, String anons, String full_text, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(title.length()==0 || anons.length()==0 || full_text.length()==0) {
            model.addAttribute("msg","У вас не заполнены некоторые поля");
            return "blog-add";
        }
        Post post = new Post(title, anons, full_text, authentication.getName());
        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") long postId, Model model) {
        if(!postRepository.existsById(postId))
            return "redirect:/blog";
        Optional<Post> post = postRepository.findById(postId);
        Post updatedPost = post.get();
        updatedPost.setViews(post.get().getViews()+1);
        updatedPost.setId(post.get().getId());
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        postRepository.save(updatedPost);
        model.addAttribute("post",res);
        model.addAttribute("comments",res.get(0).getComments());
        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long postId, Model model) {
        if(!postRepository.existsById(postId)) {
            message = "Данного поста не существует";
            return "redirect:/blog";
        }
        if(postRepository.findPostByOriginalId(postId)!=null){
            message = "Этот пост уже отредактирован. Дождитесь подтверждения";
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post",res);
        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") long postId, String title, String anons,
                                 String full_text,Model model) {
        if(title.length()==0 || anons.length()==0 || full_text.length()==0) {
            model.addAttribute("msg","Вы не заполнили некоторые поля");
            return "redirect:/blog/{id}/edit";
        }
        Post post = postRepository.findById(postId).orElseThrow();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tempAuthor = post.getAuthor();
        if (!(tempAuthor.equals(authentication.getName()) || tempAuthor.contains("," + authentication.getName() + ",")))
            tempAuthor = tempAuthor + "," + authentication.getName();
        if(tempAuthor.length()>25)
            tempAuthor = tempAuthor.substring(0,25)+",..";
        Post updatedPost = new Post(title, anons, full_text, tempAuthor, post.getId());
        if(post.isChecked())
            postRepository.save(updatedPost);
        else {
            post = updatedPost;
        }
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogPostRemove(@PathVariable(value = "id") long postId, Model model) {
        Post post = postRepository.findById(postId).orElseThrow();
        postRepository.delete(post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/comment")
    @ResponseStatus(HttpStatus.OK)
    public String blogPostComment(@PathVariable(value = "id") long postId, String comment, Model model) {
        if (comment == null) {
            return "redirect:/blog/{id}";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Comment generatedComment = new Comment(comment, authentication.getName());
        var post = postRepository.findById(postId).get();
        post.addComment(generatedComment);
        postRepository.save(post);
        return "redirect:/blog/{id}";
    }

    @RequestMapping(value = "/blog/{id}/comments/{comment_id}/remove", method = {RequestMethod.POST})
    @ResponseStatus(HttpStatus.OK)
    public String blogPostComment(@PathVariable(value = "id") long postId,
                                  @PathVariable(value = "comment_id") long commentId,
                                  String comment, Model model) {
        var post = postRepository.findById(postId).get();
        post.removeCommentById(commentId);
        postRepository.save(post);
        return "redirect:/blog/{id}";
    }
}
