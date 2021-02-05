package com.example.springExample.controllers;

import com.example.springExample.models.Comment;
import com.example.springExample.models.Post;
import com.example.springExample.models.User;
import com.example.springExample.repo.PostRepository;
import com.example.springExample.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class BlogController {
    private String message="";

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @GetMapping("/blog")
    public String blog(Model model) {
        Iterable<Post> posts = postService.findAllChecked();
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
    public String blogPostAdd(@AuthenticationPrincipal User user,
                              @Valid Post post, BindingResult bindingResult, Model model) {
        if(bindingResult.hasFieldErrors()){
            model.addAttribute("error",ControllerUtils.getStringOfErrors(bindingResult));
            return "blog-add";
        }
        postService.addPost(post,user);
        return "redirect:/blog";
    }

    @GetMapping(value = "/blog/{post}")
    public String blogDetails(@PathVariable Post post, Model model) {
        post.incrementViews();
        postRepository.save(post);
        model.addAttribute("post",post);
        model.addAttribute("comments",post.getComments());
        return "blog-details";
    }


    @GetMapping("/blog/{post}/edit")
    public String blogEdit(@PathVariable Post post, Model model) {
        if(postService.findPostByOriginalId(post.getId())!=null){
            message = "Этот пост уже отредактирован. Дождитесь подтверждения";
            return "redirect:/blog";
        }
        model.addAttribute("post",post);
        return "blog-edit";
    }

    @PostMapping("/blog/{originalPost}/edit")
    public String blogPostUpdate(@AuthenticationPrincipal User user,
                                 @PathVariable Post originalPost,
                                 @Valid Post editedPost,
                                 BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("msg",ControllerUtils.getStringOfErrors(bindingResult));
            return "redirect:/blog/{originalPost}/edit";
        }
        editedPost.updateAuthor(user);
        editedPost.setOriginalId(originalPost.getId());
        if(originalPost.isChecked())
            postRepository.save(editedPost);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{post}/remove")
    public String blogPostRemove(@PathVariable Post post) {
        postRepository.delete(post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/comment")
    public String blogPostComment(@AuthenticationPrincipal User user,
                                  @PathVariable(value = "id") Post post,
                                  String comment) {
        if (comment == null || comment.isBlank()) {
            return "redirect:/blog/{id}";
        }
        Comment generatedComment = new Comment(comment, user.getUsername());
        post.addComment(generatedComment);
        postRepository.save(post);
        return "redirect:/blog/{id}";
    }

    @RequestMapping(value = "/blog/{id}/comments/{comment_id}/remove", method = {RequestMethod.POST,RequestMethod.GET})
    public String blogPostComment(@PathVariable(value = "id") Post post,
                                  @PathVariable(value = "comment_id") long commentId) {
        post.removeCommentById(commentId);
        postRepository.save(post);
        return "redirect:/blog/{id}";
    }
}
