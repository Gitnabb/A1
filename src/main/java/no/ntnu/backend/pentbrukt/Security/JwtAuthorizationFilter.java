package no.ntnu.backend.pentbrukt.Security;

import com.auth0.jwt.JWT;
import no.ntnu.backend.pentbrukt.Entity.User;
import no.ntnu.backend.pentbrukt.Repository.UserRepository;
import no.ntnu.backend.pentbrukt.Service.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import sun.security.util.SecurityConstants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Read authorization header, where JWT token should be located

        String header = request.getHeader(JwtProperties.HEADER_STRING);

        // If header does not contain bearer or is null, pass to Spring impl and exit

        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        // If there is a header, try to get the user principal from the database and authorize
        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Pass on the filtering
        chain.doFilter(request, response);

    }


    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX,"");
        if (token != null) {
            // validate token
            String userName = JWT.require(HMAC512(JwtProperties.SECRETCODE.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();

            // Look up username token in database
            // If it is found, get user details and create a spring auth token username, pass roles etc..

            if (userName != null) {
                User user = userRepository.findByUsername(userName);
                UserPrincipal principal = new UserPrincipal(user);
                return new UsernamePasswordAuthenticationToken(userName, null, principal.getAuthorities());
            }
            return null;
        }
        return null;
    }
}
