package com.openclassrooms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.entity.User;
import com.openclassrooms.service.UserService;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * @GetMapping("/{id}")
 * public ResponseEntity<User> getUser(@PathVariable Long id) throws Exception {
 * User user = userService.getUserById(id);
 * return ResponseEntity.ok(user);
 * }
 * 
 * @GetMapping
 * public ResponseEntity<List<User>> getAllUsers() {
 * List<User> users = userService.getAllUsers();
 * return ResponseEntity.ok(users);
 * }
 */
