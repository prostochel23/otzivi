package com.example.otzivi.repositories;

import com.example.otzivi.models.Comment;
import com.example.otzivi.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProduct(Product product);
}
