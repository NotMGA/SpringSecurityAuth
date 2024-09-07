package com.openclassrooms.controllers;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.openclassrooms.model.MessageModel;
import com.openclassrooms.entity.Message;
import com.openclassrooms.service.MessageService;

import java.util.Collections;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<?> postMessage(@RequestBody MessageModel messageRequest, Authentication authentication) {
        // Check if the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthorized request");
            return ResponseEntity.status(401).body(Collections.emptyMap());
        }

        // Validate required fields are not null
        if (messageRequest.getUserId() == null || messageRequest.getMessage() == null
                || messageRequest.getRentalId() == null) {
            logger.warn("Bad request: missing required fields.");
            return ResponseEntity.badRequest().body(Collections.emptyMap());
        }

        // Log the message request
        logger.info("Message request received: " + messageRequest.getMessage());

        // Create and save the message
        Message message = messageService.createMessage(messageRequest);

        // Return success response with message
        return ResponseEntity.ok(Collections.singletonMap("message", "Message sent with success"));
    }
}