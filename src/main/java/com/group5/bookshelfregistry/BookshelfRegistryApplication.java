package com.group5.bookshelfregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class BookshelfRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookshelfRegistryApplication.class, args);
    }

}
