package com.example.slackmessageapp.conrtroller;

import com.example.slackmessageapp.exception.ResourceNotFoundException;
import com.example.slackmessageapp.model.Author;
import com.example.slackmessageapp.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping("/authors")
    public Page<Author> getAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    @PostMapping("/authors")
    public Author createAuthor(@Valid @RequestBody Author author) {
        return authorRepository.save(author);
    }

    @PutMapping("/authors/{authorId}")
    public Author updateAuthor(@PathVariable Long authorId,
                        @Valid @RequestBody Author authorRequest) {
        return authorRepository.findById(authorId)
                .map(author -> {
                    author.setName(authorRequest.getName());
                    author.setUserName(authorRequest.getUserName());
                    author.setImage(authorRequest.getImage());
                    return authorRepository.save(author);
                }).orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));
    }

    @DeleteMapping("/authors/{authorId}")
    public ResponseEntity<?> deleteAuthor(@PathVariable Long authorId) {
        return authorRepository.findById(authorId)
                .map(author -> {
                    authorRepository.delete(author);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));
    }
}
