package no.ntnu.backend.pentbrukt.Repository;

import no.ntnu.backend.pentbrukt.Entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static no.ntnu.backend.pentbrukt.Security.UserRoleConfig.USERLOGGEDIN;

@Service
public class DbInit implements CommandLineRunner {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public DbInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {

        //Delete all
        this.userRepository.deleteAll();

        // test users

        User loggedInUser = new User("kjetilhammerseth@gmail.com", passwordEncoder.encode("password123"), "kjetil", "hammerseth", "LOGGEDINUSER", "ACCESS");
        User testUser = new User("test", passwordEncoder.encode("password123"), "test", "test", "USER", "");

        List<User> users = Arrays.asList(loggedInUser, testUser);

        //push to database
        this.userRepository.saveAll(users);

    }
}
