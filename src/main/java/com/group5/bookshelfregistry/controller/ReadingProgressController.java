package com.group5.bookshelfregistry.controller;

import com.group5.bookshelfregistry.dto.readingProgress.UpdateReadingProgressRequest;
import com.group5.bookshelfregistry.service.ReadingProgressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/reading-progress")
public class ReadingProgressController {

    @Autowired
    ReadingProgressService readingProgressService;
    @PostMapping
    public ResponseEntity<?> updateReadingProgress(@RequestBody @Valid UpdateReadingProgressRequest updateReadingProgressRequest) {
       return readingProgressService.updateReadingProgress(updateReadingProgressRequest);
    }

    @GetMapping({"/book/{id}"})
    public ResponseEntity<?> getReadingProgress(@PathVariable("id") Long bookId) {
        return readingProgressService.getReadingProgress(bookId);
    }

    @GetMapping
    public ResponseEntity<?> getAllReadingProgress(@RequestParam("book-name") String bookName,@RequestParam(value =
            "limit",defaultValue = "10")int limit,@RequestParam(value = "offset",defaultValue = "0") int offset) {
        Pageable page= PageRequest.of(offset,limit);
        return readingProgressService.getAllReadingProgress(bookName,page);
    }
}
