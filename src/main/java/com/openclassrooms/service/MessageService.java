package com.openclassrooms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.entity.Message;
import com.openclassrooms.model.MessageModel;
import com.openclassrooms.Repository.MessageRepository;
import java.time.LocalDateTime;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Creates and saves a new message .
     */
    public Message createMessage(MessageModel messageRequest) {
        // Create a new Message entity
        Message message = new Message();
        message.setUserId(messageRequest.getUserId());
        message.setRentalId(messageRequest.getRentalId());
        message.setMessage(messageRequest.getMessage());
        message.setCreated_at(LocalDateTime.now());
        message.setUpdated_at(LocalDateTime.now());

        // Save the message
        return messageRepository.save(message);
    }
}
