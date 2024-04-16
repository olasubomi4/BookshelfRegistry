package com.group5.bookshelfregistry.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Table(name = "reserved-books")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;
    @ManyToOne
    private Book book;

    @CreatedDate
    private Date reservationTime;

    @PrePersist
    protected void prePersist() {
        this.reservationTime= new Date();
    }

}
