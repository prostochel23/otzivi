package com.example.otzivi.controller;

import com.example.otzivi.models.Image;
import com.example.otzivi.models.Product;
import com.example.otzivi.models.User;
import com.example.otzivi.models.enums.Role;
import com.example.otzivi.services.ProductService;
import com.example.otzivi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_MODERATOR')")
public class ModeratorController {
    private final UserService userService;
    private final ProductService productService;
    @GetMapping("/product/update/{id}")
    public String productGetEdit(@PathVariable Long id, Principal principal, Model model){

        Product product = productService.getProductById(id);
        String author = product.getUser().getEmail();
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        return "product-edit";
    }
    @PostMapping("/product/update/{id}")
    public String productPostEdit(@PathVariable Long id, Principal principal, Product product, List<Image> images){
        productService.editProduct(product, id);
        return "redirect:/product/{id}";
    }
    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return "redirect:/";
    }
}
