package com.openclassrooms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.entity.Message;
import com.openclassrooms.model.MessageModel;
import com.openclassrooms.Repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message createMessage(MessageModel messageRequest) {
        // Create a new Message entity
        Message message = new Message();
        message.setUserId(messageRequest.getUserId());
        message.setRentalId(messageRequest.getRentalId());
        message.setContent(messageRequest.getMessage());
        message.setCreatedAt(java.time.LocalDateTime.now());

        // Save the message in the database
        return messageRepository.save(message);
    }
}
