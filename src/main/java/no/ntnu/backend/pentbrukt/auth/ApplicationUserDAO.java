package no.ntnu.backend.pentbrukt.auth;

import java.util.Optional;

public interface ApplicationUserDAO {

    Optional<ApplicationUser> selectApplicationUserByUserName(String username);

}
