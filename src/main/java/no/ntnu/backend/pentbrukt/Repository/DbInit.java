package no.ntnu.backend.pentbrukt.Repository;

import no.ntnu.backend.pentbrukt.Entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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
        User loggedOutUser = new User("test@test.com", "test", "test", passwordEncoder.encode("test"));
        User testUser = new User("kjetilhammerseth@gmail.com", "Kjetil", "Hammerseth", passwordEncoder.encode("kjetil"));

        List<User> users = Arrays.asList(loggedOutUser, testUser);

        //push to database
        this.userRepository.saveAll(users);

    }
}
