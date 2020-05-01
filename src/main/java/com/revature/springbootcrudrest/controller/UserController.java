package com.revature.springbootcrudrest.controller;

import com.revature.springbootcrudrest.exception.ResourceNotFoundException;
import com.revature.springbootcrudrest.model.User;
import com.revature.springbootcrudrest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(
            @PathVariable(value = "id") Long userid
    ) throws ResourceNotFoundException {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on ::" + userid));
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user){
        return userRepository.save(user);
    }

    @PutMapping("users/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable(name = "id") Long userId,
            @Valid @RequestBody User userDetails
    ) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on ::" + userId));
        user.setEmailId(userDetails.getEmailId());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setUpdatedAt(new Date());
        final User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);

    }

    @DeleteMapping("user/{id}")
    public Map<String, Boolean> deleteUser (
            @PathVariable(value = "id") Long userId
    ) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on ::" + userId));
        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
