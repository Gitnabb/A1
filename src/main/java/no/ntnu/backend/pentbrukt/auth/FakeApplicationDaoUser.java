package no.ntnu.backend.pentbrukt.auth;

import java.util.Optional;

public class FakeApplicationDaoUser implements ApplicationUserDAO{

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUserName(String username) {
        return Optional.empty();
    }
}
