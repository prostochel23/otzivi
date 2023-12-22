package com.example.otzivi.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    @SequenceGenerator(name = "comment_seq",
            sequenceName = "comment_sequence",
            initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq")
    @Column(name = "comment_id")
    private Long id;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User user;
    @Column(name = "text")
    private String text;
    @Column(name = "estimation")
    private short estimation;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name="product_id")
    private Product product;
    @Column(name = "active")
    private boolean active;
}


