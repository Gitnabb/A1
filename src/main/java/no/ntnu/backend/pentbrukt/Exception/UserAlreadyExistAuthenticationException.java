package no.ntnu.backend.pentbrukt.Exception;

import javax.naming.AuthenticationException;

public class UserAlreadyExistAuthenticationException extends AuthenticationException {

    public UserAlreadyExistAuthenticationException(final String msg) {
        super(msg);
    }

    public UserAlreadyExistAuthenticationException() {

    }
}
