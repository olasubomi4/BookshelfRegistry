package com.group5.bookshelfregistry.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.util.Date;


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

    @CreatedDate
    private Date createAt;

    @LastModifiedDate
    private Date updatedAt;

    @ManyToOne
    private User createdBy;

    @ManyToOne(optional = true,cascade = CascadeType.PERSIST)
    private User deletedBy;

    @Transient
    private Date reservedTime;

    @PrePersist
    protected void prePersist() {
        this.createAt=new Date();
        this.updatedAt=createAt;
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt=new Date();
    }
}
