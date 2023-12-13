package com.example.otzivi.controller;

import com.example.otzivi.models.User;
import com.example.otzivi.services.ProductService;
import com.example.otzivi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ProductService productService;
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }


    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "Пользователь с email: " + user.getEmail() + " уже существует");
            return "registration";
        }
        return "redirect:/login";
    }
    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("products",user.getComments());
        return "user-info";
    }
    @GetMapping("/myfavourites")
    public String usetFavourite(Model model, Principal principal){
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("products", user.getFavourites());
        model.addAttribute("user", user);
        return "user-favourites";
    }
}
