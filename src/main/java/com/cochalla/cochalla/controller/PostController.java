package com.cochalla.cochalla.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cochalla.cochalla.dto.PostPageDto;
import com.cochalla.cochalla.service.PostServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    PostServiceImpl postService;

    @GetMapping("/{postId}")
    @ResponseBody
    public ResponseEntity<PostPageDto> getPost(@PathVariable Integer postId) {

        PostPageDto response = postService.get(postId);

        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{postId}")
    @ResponseBody
    public String deletePost(@PathVariable Integer postId) {
        return "deletePost";
    }

    @PatchMapping("/{postId}")
    @ResponseBody
    public String patchPost(@PathVariable Integer postId, @RequestBody Boolean isPublic) {
        return "pathcPost";
    }

    @PostMapping("/{postId}/comment")
    @ResponseBody
    public String postComment(@PathVariable Integer postId, @RequestBody String comment) {
        return "postComment";
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    @ResponseBody
    public String deleteComment(@PathVariable Integer postId, @PathVariable Integer commentId) {
        return "deleteComment";
    }

    @PostMapping("/{postId}/like")
    @ResponseBody
    public String postLike(@PathVariable Integer postId, @RequestBody Boolean isLike) {
        return "postLike";
    }
    
}
