package com.openclassrooms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.entity.Message;
import com.openclassrooms.model.MessageModel;
import com.openclassrooms.Repository.MessageRepository;

/**
 * Service responsible for managing messages in the application.
 * This service allows the creation and persistence of messages.
 */
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    /**
     * Constructor for MessageService.
     *
     * @param messageRepository The repository to interact with Message data.
     */
    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Creates and saves a new message based on the provided request model.
     *
     * @param messageRequest The message model containing the data for the new
     *                       message.
     * @return The saved Message entity.
     */
    public Message createMessage(MessageModel messageRequest) {
        // Create a new Message entity
        Message message = new Message();
        message.setUserId(messageRequest.getUserId());
        message.setRentalId(messageRequest.getRentalId());
        message.setContent(messageRequest.getMessage());
        message.setCreated_at(java.time.LocalDateTime.now());

        // Save the message in the database and return it
        return messageRepository.save(message);
    }
}
