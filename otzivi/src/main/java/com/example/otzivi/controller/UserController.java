package com.example.otzivi.controller;

import com.example.otzivi.models.User;
import com.example.otzivi.repositories.UserRepository;
import com.example.otzivi.services.EmailService;
import com.example.otzivi.services.ProductService;
import com.example.otzivi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final ProductService productService;
    private final EmailService emailService;
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
        String[] patterns = new String[] {"\\W","\\d","[a-zA-Z]"};
        String password = user.getPassword();
        if (password.length() < 6)
        {
            model.addAttribute("errorMessage", "Длина пароля должна быть больше 6 символов");
            return "registration";
        }
        for (String pattern : patterns)
        {
            if (!Pattern.compile(pattern).matcher(password).find()) {
                model.addAttribute("errorMessage", "Пароль должен содержать буквенные, цифровые и специальные символы");
                return "registration";
            }
        }
        Long id = userService.createUser(user);
        if (id == -1) {
            model.addAttribute("errorMessage", "Пользователь с email: " + user.getEmail() + " уже существует");
            return "registration";
        }
        model.addAttribute("result", false);
        return "/hello";
    }
    @GetMapping("/confirm/{id}/{code}")
    public String postConfirm(Model model,@PathVariable("code") String code,@PathVariable("id") Long id) {
        model.addAttribute("result", userService.confirmUser(id,code));
        return "/hello";
    }
    @GetMapping("/recovery")
    public String getRecovery(Model model) {
        model.addAttribute("step", 1);
        return "/recovery";
    }
    @PostMapping("/recovery")
    public String PostRecovery(Model model,String email) {
        if (!userService.tryRecoverPass(email))
        {
            model.addAttribute("errorMessage", "Пользователь с email: " + email + " не существует");
            model.addAttribute("step", -1);
            return "/recovery";
        }
        model.addAttribute("step", 2);
        return "/recovery";
    }
    @GetMapping("/recovery/{id}/{code}")
    public String getRecoveryHard(Model model,@PathVariable("code") String code,@PathVariable("id") Long id) {
        model.addAttribute("step", 3);
        model.addAttribute("id", id);
        model.addAttribute("code", code);
        return "/recovery";
    }
    @PostMapping("/recovery/{id}/{code}")
    public String postRecoveryHard(Model model,@PathVariable("code") String code,@PathVariable("id") Long id,String password) {
        if (!userService.recoveryPass(id,code,password))
        {
            model.addAttribute("errorMessage", "Ошибка ссылки");
            model.addAttribute("step", -1);
            return "/recovery";
        }
        model.addAttribute("step", 4);
        return "/recovery";
    }
    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("products",user.getComments());
        return "user-info";
    }
    @GetMapping("/myfavourites")
    public String userFavourite(Model model, Principal principal){
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("products", user.getFavourites());
        model.addAttribute("user", user);
        return "user-favourites";
    }
    @GetMapping("/debug")
    public String debug(Model model, Principal principal){
        model.addAttribute("debug-info", principal != null);
        return "debug";
    }
}
