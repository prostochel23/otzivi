package com.example.otzivi.controller;

import com.example.otzivi.models.Comment;
import com.example.otzivi.models.Image;
import com.example.otzivi.models.Product;
import com.example.otzivi.models.User;
import com.example.otzivi.repositories.ProductRepository;
import com.example.otzivi.services.CommentService;
import com.example.otzivi.services.ProductService;
import com.example.otzivi.models.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/makecomment/{id}")
    public String deleteComment(@PathVariable Long id, Principal principal, Comment comment){
        commentService.saveComment(principal,id,comment);
        return "redirect:/product/{id}";
    }
    //@PreAuthorize("hasAuthority('ROLE_UPPER')")
    @GetMapping("/hide/{id}")
    public String hideComment(@PathVariable Long id, Principal principal){
        Long productId = commentService.disableComment(principal,id);
        return "redirect:/product/"+productId;
    }
    //@PreAuthorize("hasAuthority('ROLE_UPPER')")
    @GetMapping("/show/{id}")
    public String showComment(@PathVariable Long id, Principal principal){
        Long productId = commentService.enableComment(principal,id);
        return "redirect:/product/"+productId;
    }

}
