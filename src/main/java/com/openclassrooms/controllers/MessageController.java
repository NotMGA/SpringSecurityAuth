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

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.Collections;

import java.util.HashMap;
import java.util.Map;

/**
 * MessageController handles API requests for creating and managing messages.
 */
@RestController
@RequestMapping("/api/messages")

public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final MessageService messageService;

    /**
     * Constructor to inject dependencies for MessageService.
     * 
     * @param messageService the message service to handle message operations
     */
    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * Handles the request to post a message. Verifies if the user is authenticated
     * and that the request contains the
     * required fields before creating and saving the message.
     * 
     * @param messageRequest the request body containing message details
     * @param authentication the authentication object to verify if the user is
     *                       logged in
     * @return a ResponseEntity indicating the result of the message creation
     */
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful "),
            @ApiResponse(responseCode = "401", description = "Unauthorized "),
            @ApiResponse(responseCode = "400", description = "Bad request ")
    })
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

        // Create and save the message
        Message message = messageService.createMessage(messageRequest);

        // Return success response with message
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Message sent with success");
        return ResponseEntity.ok(successResponse);
    }
}