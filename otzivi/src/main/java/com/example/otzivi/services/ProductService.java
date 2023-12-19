package com.example.otzivi.services;

import com.example.otzivi.models.Comment;
import com.example.otzivi.models.Image;
import com.example.otzivi.models.Product;
import com.example.otzivi.models.User;
import com.example.otzivi.repositories.CommentRepository;
import com.example.otzivi.repositories.ProductRepository;
import com.example.otzivi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    static public List<String> categories = new ArrayList<>();

    public Page<Product> listProducts(String title, String category, Pageable pageable) {
        if (title != null) return productRepository.findByTitle(title,pageable);
        else if (category != null) return productRepository.findByCategory(category,pageable);
        return productRepository.findAll(pageable);
    }
    public void makeCategories()
    {
        for (Product product : productRepository.findAll())
        {
            String currentCategory = product.getCategory();
            if (!categories.contains(currentCategory))
                categories.add(currentCategory);
        }
        if (categories.isEmpty())
            categories.add("Общее");
    }

    public void saveProduct(Principal principal, Product product, MultipartFile file1, MultipartFile file2, MultipartFile file3, String anotherCategory) throws IOException {
        product.setUser(getUserByPrincipal(principal));
        Image image1;
        Image image2;
        Image image3;
        List<Image> images = new ArrayList<>();
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            image1.setProduct(product);
            images.add(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            image2.setProduct(product);
            images.add(image2);
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            image3.setProduct(product);
            images.add(image3);
        }
        if (anotherCategory.length()!=0) {
            product.setCategory(anotherCategory);
            if (!categories.contains(anotherCategory))
                categories.add(anotherCategory);
        }
        product.setImages(images);
        product.setPreviewImageId(images.get(0).getId());
        product.setRating(0);
        product.setTotalEstimation(0);
        product.setTotalAmountOfEstimation(0);
        log.info("Saving new Product. Title: {}; Author email: {}", product.getTitle(), product.getUser().getEmail());
        productRepository.save(product);
    }
    public void editProduct(Product product, Long id) {
        Product oldProduct = getProductById(id);
        oldProduct.setDescription(product.getDescription());
        oldProduct.setTitle(product.getTitle());
        log.info("Changing the Product. Title: {}; Author email: {}", oldProduct.getTitle(), oldProduct.getUser().getEmail());
        productRepository.save(oldProduct);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        String name = principal.getName();
        return this.userRepository.findByEmail(name);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product getProductById(Long id) {
       return productRepository.findById(id).orElse(null);
    }
    public void addFavouriteProduct(Long id, Principal principal)
    {
        User user = userRepository.findByEmail(principal.getName());
        List<Product> oldList = user.getFavourites();
        oldList.add(getProductById(id));
        user.setFavourites(oldList);
        userRepository.save(user);
    }
    public void deleteFavouriteProduct(Long id, Principal principal)
    {
        User user = userRepository.findByEmail(principal.getName());
        List<Product> oldList = user.getFavourites();
        Product productToDelete = getProductById(id);
        oldList.remove(productToDelete);
        user.setFavourites(oldList);
        userRepository.save(user);
    }
}
