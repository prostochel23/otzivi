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
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Set;

import static com.example.otzivi.services.ProductService.categories;

@Controller
@RequiredArgsConstructor
public class ProductController {
    // TODO: Rewrite with check links
    private final ProductService productService;
    private final CommentService commentService;
    @GetMapping("/")
    public String products(@RequestParam(name = "title", required = false) String title,
                           @RequestParam(name = "category", required = false) String category, Principal principal, Model model,
                           @PageableDefault(sort = {"rating"}, direction = Sort.Direction.DESC) Pageable pageable){
        if (categories.isEmpty())
            productService.makeCategories();
        Page<Product> page = productService.listProducts(title,category,pageable);
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("products", page.getContent());
        model.addAttribute("user", user);
        model.addAttribute("page", page);
        model.addAttribute("url", "/");
        var a = categories.toArray();
        model.addAttribute("categories", categories);
        return "products";
    }
    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model, Principal principal){
        Product product = productService.getProductById(id);
        User user = productService.getUserByPrincipal(principal);
        boolean edit_allowed = user.getRoles().contains(Role.ROLE_MODERATOR);
        boolean hide_allowed = user.getRoles().contains(Role.ROLE_UPPER);
        List<Product> favouriteProducts = user.getFavourites();
        boolean alreadyLoved = favouriteProducts.contains(product);
        model.addAttribute("alreadyLoved", alreadyLoved);
        model.addAttribute("product", product);
        model.addAttribute("comments", product.getComments());
        model.addAttribute("images", product.getImages());
        model.addAttribute("edit_allowed",edit_allowed);
        model.addAttribute("hide_allowed",hide_allowed);
        return "product-info";
    }


    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3, Product product, Principal principal,
                                @RequestParam("anotherCategory") String anotherCategory) throws IOException {
        productService.saveProduct(principal, product, file1, file2, file3,anotherCategory);
        return "redirect:/";
    }

    @GetMapping("/product/addFavourite/{id}")
    public String addFavouriteProduct(@PathVariable Long id, Principal principal){
        productService.addFavouriteProduct(id, principal);
        return "redirect:/product/{id}";
    }
    @GetMapping("/product/deleteFavourite/{id}")
    public String deleteFavouriteProduct(@PathVariable Long id, Principal principal){
        productService.deleteFavouriteProduct(id, principal);
        return "redirect:/product/{id}";
    }
    // TODO : При удалении сделать обработчики существования записи
}
