package com.group5.bookshelfregistry.entities;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book-category")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class BookCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = true)
    private String name;
}
