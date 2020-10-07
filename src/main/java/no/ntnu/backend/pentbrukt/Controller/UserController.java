package no.ntnu.backend.pentbrukt.Controller;


import no.ntnu.backend.pentbrukt.Entity.User;
import no.ntnu.backend.pentbrukt.Exception.ResourceNotFoundException;
import no.ntnu.backend.pentbrukt.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users/")
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
    }

    // Get all users
    @GetMapping("get-all-users")
    public List<User> getAllUsers() {

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
    @PostMapping("new-user")
    public User registerUser(@RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(1);
        user.setRoles("LOGGEDINUSER");
        user.setPermissions("ACCESS");
        System.out.println(user.getUsername() + " registered!");
        return this.userRepository.save(user);

    }

}
