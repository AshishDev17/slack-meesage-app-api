package com.example.slackmessageapp.conrtroller;

import com.example.slackmessageapp.exception.ResourceNotFoundException;
import com.example.slackmessageapp.model.Message;
import com.example.slackmessageapp.repository.AuthorRepository;
import com.example.slackmessageapp.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MessageController {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/authors/{authorId}/messages")
    public List<Message> getMessagesByAuthorId(@PathVariable Long authorId) {
        return messageRepository.findByAuthorId(authorId);
    }

    @PostMapping("/authors/{authorId}/messages")
    public Message addMessage(@PathVariable Long authorId,
                              @Valid @RequestBody Message  message) {
        return authorRepository.findById(authorId)
                .map(author -> {
                    message.setAuthor(author);
                    return messageRepository.save(message);
                }).orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));
    }

    @PutMapping("/authors/{authorId}/messages/{messageId}")
    public Message updateMessage(@PathVariable Long authorId,
                                 @PathVariable Long messageId,
                                 @Valid @RequestBody Message messageRequest) {
        if(!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Author not found with id: " + authorId);
        }

        return messageRepository.findById(messageId)
                .map(message -> {
                    message.setContent(messageRequest.getContent());
                    return messageRepository.save(message);
                }).orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + messageId));
    }

    @DeleteMapping("/authors/{authorId}/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long authorId,
                                           @PathVariable Long messageId) {
        if(!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Author not found with id: " + authorId);
        }

        return messageRepository.findById(messageId)
                .map(message -> {
                    messageRepository.delete(message);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + messageId));
    }
}
