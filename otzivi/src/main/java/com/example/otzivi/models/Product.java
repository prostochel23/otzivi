package com.example.otzivi.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<Image> images = new ArrayList<>();
    private Long previewImageId;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;
    private LocalDateTime dateOfCreated;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "product")
    private List<Comment> comments = new ArrayList<>();
    @Column(name = "totalEstimation")
    private int totalEstimation;
    @Column(name = "totalAmountOfEstimation")
    private int totalAmountOfEstimation;
    @Column(name = "rating")
    private float rating;
    @PrePersist
    private void init(){
        dateOfCreated = LocalDateTime.now();
    }
    public void addImageToProduct(Image image) {
        image.setProduct(this);
        images.add(image);
    }
}
