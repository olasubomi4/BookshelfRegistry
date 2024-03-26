package com.group5.bookshelfregistry.entities;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book-category")
@Getter
@Setter
public class BookCategory {
    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(unique = true,nullable = false)
    private String name;
}
