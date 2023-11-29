package com.example.otzivi.controller;

import com.example.otzivi.models.Product;
import com.example.otzivi.models.User;
import com.example.otzivi.repositories.ProductRepository;
import com.example.otzivi.services.ProductService;
import com.example.otzivi.models.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;
    @GetMapping("/")
    public String products(@RequestParam(name = "title", required = false) String title,Principal principal, Model model){
        List<Product> products = productService.listProducts(title);
        User user = productService.getUserByPrincipal(principal);
        model.addAttribute("products", products);
        model.addAttribute("user", user);
        return "products";
    }
    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model){
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        return "product-info";
    }
    @GetMapping("/product/update/{id}")
    public String productGetEdit(@PathVariable Long id, Principal principal, Model model){
        Product product = this.productService.getProductById(id);
        String first = product.getUser().getEmail();
        String second = principal.getName();
        //          productService.getUserByPrincipal(principal)
//        model.addAttribute("user", user);
        // TODO: переписать условие, чтобы проверял роли нормально
        if (!(product.getUser().getEmail().equals(principal.getName())) )
//                || !user.getRoles().contains(Role.ROLE_ADMIN))
            return "403";
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        return "product-edit";
    }
    @PostMapping("/product/update/{id}")
    public String productPostEdit(@PathVariable Long id, Principal principal, Product product){
        // TODO: переписать условие, чтобы проверял роли нормально
        Product anotherProduct = this.productService.getProductById(id);
        String first = anotherProduct.getUser().getEmail();
        String second = principal.getName();
        if (!(first.equals(principal.getName())) )
            return "403";
        productService.editProduct(product, id);
        return "redirect:/product/{id}";
    }

    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1, @RequestParam("file2") MultipartFile file2, @RequestParam("file3") MultipartFile file3, Product product, Principal principal) throws IOException {
        productService.saveProduct(principal, product, file1, file2, file3);
        return "redirect:/";
    }
    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return "redirect:/";
    }
}
