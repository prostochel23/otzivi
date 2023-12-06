package com.example.otzivi.services;

import com.example.otzivi.models.Comment;
import com.example.otzivi.models.Image;
import com.example.otzivi.models.Product;
import com.example.otzivi.models.User;
import com.example.otzivi.models.enums.Role;
import com.example.otzivi.repositories.CommentRepository;
import com.example.otzivi.repositories.ProductRepository;
import com.example.otzivi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ProductService productService;

//    public List<Product> listProducts(String title) {
//        if (title != null) return productRepository.findByTitle(title);
//        return productRepository.findAll();
//    }

    public boolean saveComment(Principal principal, long productID, Comment comment) {

        if (0 > comment.getEstimation() || comment.getEstimation() > 5)
            return false;
        Product product = productService.getProductById(productID);
        int currentScore = product.getTotalEstimation() + comment.getEstimation();
        int currentAmount = product.getTotalAmountOfEstimation() + 1;
        float currentRating = (float) currentScore /currentAmount;
        comment.setUser(productService.getUserByPrincipal(principal));
        comment.setActive(true);
        List<Comment> comments = product.getComments();
        comments.add(comment);
        product.setComments(comments);
        product.setTotalAmountOfEstimation(currentAmount);
        product.setTotalEstimation(currentScore);
        product.setRating(Math.round(currentRating));
        productRepository.save(product);
//        commentRepository.save(comment);
        log.info("Saving new Comment. Post's title: {}; Author email: {}", product.getTitle(), comment.getUser().getEmail());
        return true;
    }
    public void editComment(Comment comment, Long id) {
        Comment oldComment = commentRepository.findById(id).orElse(null);
        assert oldComment != null;
        oldComment.setText(comment.getText());
        if (0 > comment.getEstimation() || comment.getEstimation() > 5)
            return;
        if (oldComment.getEstimation() != comment.getEstimation())
        {
            Product product = oldComment.getProduct();
            product.setTotalEstimation(product.getTotalEstimation() - oldComment.getEstimation() + comment.getEstimation());
            product.setRating((float) product.getTotalEstimation() /product.getTotalAmountOfEstimation());
            oldComment.setEstimation(comment.getEstimation());
            productRepository.save(product);
        }
        log.info("Changing the Product. Title: {}; Author email: {}", oldComment.getProduct().getTitle(), oldComment.getUser().getEmail());
        commentRepository.save(oldComment);
    }
//
    public void disableComment(Principal principal, Long id) {
        User user = productService.getUserByPrincipal(principal);
        Comment comment = commentRepository.findById(id).orElse(null);
        assert comment != null;
        if (comment.getUser().getName().equals(user.getName()) || user.getRoles().contains(Role.ROLE_UPPER)) {
            comment.setActive(false);
            Product product = productService.getProductById(comment.getProduct().getId());
            product.setTotalAmountOfEstimation(product.getTotalAmountOfEstimation() - 1);
            product.setTotalEstimation(product.getTotalEstimation()-comment.getEstimation());
            product.setRating((float) product.getTotalEstimation() /product.getTotalAmountOfEstimation());
            productRepository.save(product);
            commentRepository.save(comment);
        }
    }
    public void enableComment(Principal principal, Long id) {
        User user = productService.getUserByPrincipal(principal);
        Comment comment = commentRepository.findById(id).orElse(null);
        assert comment != null;
        if (comment.getUser().getName().equals(user.getName()) || user.getRoles().contains(Role.ROLE_UPPER)) {
            comment.setActive(true);
            Product product = productService.getProductById(comment.getProduct().getId());
            product.setTotalAmountOfEstimation(product.getTotalAmountOfEstimation() + 1);
            product.setTotalEstimation(product.getTotalEstimation()+comment.getEstimation());
            product.setRating((float) product.getTotalEstimation() /product.getTotalAmountOfEstimation());
            productRepository.save(product);
            commentRepository.save(comment);
        }
    }
    public void deleteComment(Principal principal, Long id){
        User user = productService.getUserByPrincipal(principal);
        Comment comment = commentRepository.findById(id).orElse(null);
        assert comment != null;
        if (comment.getUser().getName().equals(user.getName()) || user.getRoles().contains(Role.ROLE_UPPER)) {
            disableComment(principal, id);
            commentRepository.deleteById(id);
        }
    }
//
//    private Image toImageEntity(MultipartFile file) throws IOException {
//        Image image = new Image();
//        image.setName(file.getName());
//        image.setOriginalFileName(file.getOriginalFilename());
//        image.setContentType(file.getContentType());
//        image.setSize(file.getSize());
//        image.setBytes(file.getBytes());
//        return image;
//    }
//
//    public void deleteProduct(Long id) {
//        productRepository.deleteById(id);
//    }
//
//    public Product getProductById(Long id) {
//        return productRepository.findById(id).orElse(null);
//    }
}
