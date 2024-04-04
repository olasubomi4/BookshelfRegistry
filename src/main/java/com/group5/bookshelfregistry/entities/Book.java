package com.group5.bookshelfregistry.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @Column(nullable = false,unique = true)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false,unique = true)
    private String isbn;
    @ManyToOne(optional = true,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "book_category_id", referencedColumnName = "id",nullable = true)
    private BookCategory bookCategory;

    @Column(name = "book_location")
    private String bookLocation;
    @Column(name = "book_image_location")
    private String bookImageLocation;
}
