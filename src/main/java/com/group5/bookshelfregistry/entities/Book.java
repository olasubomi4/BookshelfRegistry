package com.group5.bookshelfregistry.entities;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;

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
    @ManyToOne(optional = true,cascade = {CascadeType.ALL, CascadeType.REMOVE})
    @JoinColumn(name = "book_category_id", referencedColumnName = "id",nullable = true)
    private BookCategory bookCategory;

    private String bookLocation;
}
