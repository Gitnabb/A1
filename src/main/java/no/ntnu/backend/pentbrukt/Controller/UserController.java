package no.ntnu.backend.pentbrukt.Controller;


import no.ntnu.backend.pentbrukt.Entity.Listing;
import no.ntnu.backend.pentbrukt.Entity.User;
import no.ntnu.backend.pentbrukt.Exception.ResourceNotFoundException;
import no.ntnu.backend.pentbrukt.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Get all users
    @GetMapping("users")
    public List<User> getAllUsers(){

        return this.userRepository.findAll();

    }

    // Get user by user id
    @GetMapping("users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userid)
            throws ResourceNotFoundException {
        // Lookup
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new ResourceNotFoundException("No user matching ' " + userid + " '"));

        return ResponseEntity.ok().body(user);
    }

    // Register a user

    @PostMapping("users")
    public User registerUser(@RequestBody User user){
        return this.userRepository.save(user);
    }

}
