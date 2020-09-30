package no.ntnu.backend.pentbrukt.Repository;

import no.ntnu.backend.pentbrukt.Entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class DbInit implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DbInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {

        //Delete all
        this.userRepository.deleteAll();

        // test users

        User loggedInUser = new User("kjetilhammerseth@gmail.com", passwordEncoder.encode("password123"), "USERLOGGEDIN", "ACCESS");
        User testUser = new User("test@gmail.com", passwordEncoder.encode("password123"), "USER", "");

        List<User> users = Arrays.asList(loggedInUser, testUser);

        //push to database
        this.userRepository.saveAll(users);

    }
}
